package ru.zarina.zarina.ui.screens.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.datasource.cache.Cache
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.FavoriteState
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.exception.NotFoundException
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.base.operation.OperationKey
import ru.zarina.zarina.ui.common.base.operation.OperationTracker
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import ru.zarina.zarina.utils.coroutine.mapState
import ru.zarina.zarina.utils.isNetworkException

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

    private val _errorType = MutableStateFlow<ErrorType?>(null)
    val errorType = _errorType.asStateFlow()

    private val _product = MutableStateFlow<Product?>(null)
    val product = _product.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val completeLookProducts = _product
        .distinctUntilChangedBy { it?.id }
        .mapLatest { product ->
            operationTracker.track(Operation.LOADING_COMPLETE_LOOK) {
                if (product?.isLookPart == true) {
                    interactor.getCompleteLook(product)
                        .getOrDefault(emptyList())
                        .toPersistentList()
                } else {
                    persistentListOf()
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, persistentListOf())

    @OptIn(ExperimentalCoroutinesApi::class)
    val similarProducts = _product
        .distinctUntilChangedBy { it?.id }
        .mapLatest { product ->
            operationTracker.track(Operation.LOADING_RECOMMENDATIONS) {
                if (product != null) {
                    interactor.getRecommendations(product)
                        .getOrDefault(emptyList())
                        .toPersistentList()
                } else {
                    persistentListOf()
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, persistentListOf())

    @OptIn(ExperimentalCoroutinesApi::class)
    val deliveryAvailability = _product
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

    init {
        productId
            .mapLatest { id -> loadProduct(id) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    }

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
        viewModelScope.launch {
            loadProduct(productId.value)
        }
    }

    fun onFavoriteChange(isFavorite: Boolean) {
        val product = product.value ?: return
        _product.update { it?.copy(favorite = FavoriteState(isFavorite)) }
        viewModelScope.launch {
            interactor.setIsFavorite(product, isFavorite)
                .onFailure {
                    _product.update { it?.copy(favorite = product.favorite.copy(isErrorReset = true)) }
                }
        }
    }

    private suspend fun loadProduct(id: Product.Id) {
        operationTracker.track(Operation.LOADING_PRODUCT) {
            interactor.getProduct(id)
                .onSuccess {
                    _product.value = it
                    _errorType.value = null
                }
                .getOrElse { throwable ->
                    _errorType.value = when {
                        throwable.isNetworkException() -> ErrorType.NETWORK
                        throwable is NotFoundException -> ErrorType.NOT_FOUND
                        else -> ErrorType.GENERIC
                    }
                    null
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

}
