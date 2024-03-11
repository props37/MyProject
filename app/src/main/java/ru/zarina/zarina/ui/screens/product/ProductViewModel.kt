package ru.zarina.zarina.ui.screens.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.datasource.cache.Cache
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.base.operationtracker.OperationKey
import ru.zarina.zarina.base.operationtracker.OperationTracker
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.domain.old.exception.NotFoundException
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.navigation.old.destinations.Destinations
import ru.zarina.zarina.utils.coroutine.mapState
import ru.zarina.zarina.utils.isNetworkException
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class ProductViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: ProductInteractor,
    cache: Cache,
) : ViewModel(),
    ISideEffectSource<ProductViewModel.SideEffect> by SideEffectQueue() {

    private val operationTracker = OperationTracker()

    val cache = MutableStateFlow(cache).asStateFlow()
    private val productId = savedStateHandle.getStateFlow(
        key = Destinations.Product.ARGUMENT_PRODUCT_ID,
        initialValue = ""
    ).mapState(viewModelScope) { Product.Id(it) }

    private val productReloadTrigger = MutableSharedFlow<Unit>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    ).apply { tryEmit(Unit) }
    private val productResult = combine(productId, productReloadTrigger) { id, _ -> id }
        .flatMapLatest { id ->
            operationTracker.track(Operation.LOADING_PRODUCT) {
                interactor.getProduct(id)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)


    val errorType = productResult.map { result ->
        val throwable = result?.exceptionOrNull() ?: return@map null
        when {
            throwable.isNetworkException() -> ErrorType.NETWORK
            throwable is NotFoundException -> ErrorType.NOT_FOUND
            else -> ErrorType.GENERIC
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val product = productResult.mapState(viewModelScope) { it?.getOrNull() }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val completeLookResult = product
        .distinctUntilChangedBy { it?.id }
        .flatMapLatest { product ->
            operationTracker.track(Operation.LOADING_COMPLETE_LOOK) {
                product?.let { interactor.getCompleteLook(it) } ?: flowOf(null)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val completeLookProducts = completeLookResult.mapState(viewModelScope) {
        it?.getOrNull().orEmpty().toPersistentList()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val similarProductsResult = product
        .distinctUntilChangedBy { it?.id }
        .flatMapLatest { product ->
            operationTracker.track(Operation.LOADING_RECOMMENDATIONS) {
                product?.let { interactor.getRecommendations(it) } ?: flowOf(null)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val similarProducts = similarProductsResult.mapState(viewModelScope) {
        it?.getOrNull().orEmpty().toPersistentList()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val deliveryAvailability = product
        .distinctUntilChangedBy { it?.id }
        .mapLatest { product ->
            operationTracker.track(Operation.LOADING_DELIVERY_AVAILABILITY) {
                if (product != null)
                    interactor.getDeliveryAvailability(product).getOrNull()
                else
                    null
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val isProductLoaderVisible = operationTracker
        .isOperationOngoing(
            Operation.LOADING_PRODUCT,
            Operation.LOADING_COMPLETE_LOOK,
            Operation.LOADING_RECOMMENDATIONS,
            Operation.LOADING_DELIVERY_AVAILABILITY
        )
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    private val _shakingFavorites = MutableStateFlow(emptySet<Product.Id>())
    val shakingFavorites = _shakingFavorites.mapState(viewModelScope) { it.toPersistentSet() }

    fun onVariantClick(variant: Product.Variant) {
        savedStateHandle[Destinations.Product.ARGUMENT_PRODUCT_ID] = variant.id.value
    }

    fun onShareClick() {
        val url = product.value?.url
        if (url != null) sideEffect(SideEffect.ShareText(url.value))
    }

    fun onProductClick(product: Product) {
        sideEffect(SideEffect.ShowProduct(product))
    }

    fun onBackClick() {
        sideEffect(SideEffect.GoBack)
    }

    fun onPickupClick(product: Product) {
        sideEffect(SideEffect.ShowPickup(product))
    }

    fun onRefreshClick() {
        productReloadTrigger.tryEmit(Unit)
    }

    fun onFavoriteChange(product: Product, isFavorite: Boolean) {
        viewModelScope.launch {
            interactor.setIsFavorite(product, isFavorite)
                .onFailure {
                    _shakingFavorites.update { it + product.id }
                    delay(FAVORITE_SHAKE_DURATION)
                    _shakingFavorites.update { it - product.id }
                }
        }
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        data class ShareText(val text: String) : SideEffect
        data class ShowProduct(val product: Product) : SideEffect
        data class ShowPickup(val product: Product) : SideEffect
        object GoBack : SideEffect
    }

    enum class Operation : OperationKey {
        LOADING_PRODUCT,
        LOADING_COMPLETE_LOOK,
        LOADING_RECOMMENDATIONS,
        LOADING_DELIVERY_AVAILABILITY
    }

    enum class ErrorType {
        NETWORK,
        NOT_FOUND,
        GENERIC
    }

    companion object {
        private val FAVORITE_SHAKE_DURATION = 1.seconds
    }

}
