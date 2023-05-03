package ru.zarina.zarina.ui.screens.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.datasource.cache.Cache
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.exception.NotFoundException
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.base.operation.OperationKey
import ru.zarina.zarina.ui.common.base.operation.OperationTracker
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import ru.zarina.zarina.utils.isNetworkException
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: ProductInteractor,
    cache: Cache,
) : ViewModel(),
    ISideEffectSource<ProductViewModel.SideEffect> by SideEffectQueue() {

    private val operationTracker = OperationTracker()

    val cache = MutableStateFlow(cache).asStateFlow()
    private val productId = savedStateHandle.getStateFlow(
        key = Destinations.PRODUCT.ARGUMENT_PRODUCT_ID,
        initialValue = ""
    ).map { Product.Id(it) }

    @OptIn(ExperimentalCoroutinesApi::class)
    val product = productId
        .mapLatest { id ->
            operationTracker.track(Operation.LOADING_PRODUCT) {
                interactor.getProduct(id)
                    .onSuccess {
                        setNetworkErrorState(isVisible = false)
                    }
                    .getOrElse { throwable ->
                        when {
                            throwable.isNetworkException() -> setNetworkErrorState(isVisible = true)
                            throwable is NotFoundException -> { /* TODO show not found error */
                            }
                        }
                        null
                    }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val completeLookProducts = product
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
    val similarProducts = product
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
    val deliveryAvailability = product
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

    fun onVariantClick(variant: Product.Variant) {
        savedStateHandle[Destinations.PRODUCT.ARGUMENT_PRODUCT_ID] = variant.id.value
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

    private fun setNetworkErrorState(isVisible: Boolean) {
        // TODO
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        data class ShareText(val text: String) : SideEffect
        data class ShowProduct(val product: Product) : SideEffect
        object GoBack : SideEffect
    }

    enum class Operation : OperationKey {
        LOADING_PRODUCT,
        LOADING_COMPLETE_LOOK,
        LOADING_RECOMMENDATIONS,
        LOADING_DELIVERY_AVAILABILITY
    }

}
