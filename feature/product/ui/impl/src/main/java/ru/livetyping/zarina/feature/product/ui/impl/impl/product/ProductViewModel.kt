package ru.livetyping.zarina.feature.product.ui.impl.impl.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.analytics.model.Screen
import ru.livetyping.zarina.core.coroutinesutil.WhileUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.combineMore
import ru.livetyping.zarina.core.domain.analytics.toAppMetricaProduct
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage2
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.component.ProductComponent
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductState
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
internal class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: ProductDependencies,
) : ViewModel(), SideEffectSource<ProductSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val productComponent = ProductComponent(
        getProductUseCase = deps.getProduct,
        getProductTotalLookUseCase = deps.getProductTotalLook,
        getSimilarProductsUseCase = deps.getSimilarProducts,
        getWishlistProductIdsFlowUseCase = deps.getWishlistProductIdsFlow,
        getCartProductIdsFlowUseCase = deps.getCartProductIdsFlow,
        coroutineScope = viewModelScope,
    )

    private val navEntry = savedStateHandle.toRoute<ProductFeature.NavEntry.StartNavEntry>()

    init {
        productComponent.setProductId(navEntry.getProductId())
    }

    private var reportScreenCreatedJob: Job? = null

    private val productStateBuilder = ProductState.Builder()

    val productState: StateFlow<ProductState> = combineMore(
        productComponent.productResult,
        productComponent.isProductLoading,
        productComponent.totalLookProductsResult,
        productComponent.areTotalLookProductsLoading,
        productComponent.similarProductsResult,
        productComponent.areSimilarProductsLoading,
    ) { productResult, isProductLoading, totalLookProductsResult, areTotalLookProductsLoading,
        similarProductsResult, areSimilarProductsLoading ->

        productStateBuilder.build(
            productResult = productResult,
            isProductLoading = isProductLoading,
            totalLookProductsResult = totalLookProductsResult,
            areTotalLookProductsLoading = areTotalLookProductsLoading,
            similarProductsResult = similarProductsResult,
            areSimilarProductsLoading = areSimilarProductsLoading,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = ProductState.Loading,
    )

    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event) {
            LifecycleEvent.ON_CREATE -> onScreenCreated()
            else -> Unit
        }
    }

    // TODO: [Top] Implement
    fun onProductEvent(event: ProductEvent) {
        when (event) {
            ProductEvent.BackClicked -> onBackClicked()
            ProductEvent.ShareClicked -> onShareClicked()
            is ProductEvent.ProductColorClicked -> onProductColorClicked(event)
            is ProductEvent.ProductClicked -> onProductClicked(event)
            is ProductEvent.AddProductToWishlistClicked -> onAddProductToWishlistClicked(event)
            ProductEvent.SizeTableClicked -> onSizeTableClicked()
            ProductEvent.ProductRefreshTriggered -> onProductRefreshTriggered()
            ProductEvent.TotalLookProductRefreshTriggered -> onTotalLookProductRefreshTriggered()
            ProductEvent.SimilarProductRefreshTriggered -> onSimilarProductRefreshTriggered()
        }
    }

    private fun onScreenCreated() {
        reportScreenCreated()
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProductScreenAction.BackClicked
            emitSideEffect(ProductSideEffect.Navigate(action))
        }
    }

    private fun onShareClicked() {
        navigationThrottler.throttle {
            viewModelScope.launch {
                val product = productComponent.awaitProduct()
                val shareUrl = product?.shareUrl?.value
                if (!shareUrl.isNullOrBlank()) {
                    emitSideEffect(ProductSideEffect.Share(shareUrl))
                }
            }
        }
    }

    private fun onProductColorClicked(event: ProductEvent.ProductColorClicked) {
        productComponent.setProductId(event.color.productId)
    }

    private fun onProductClicked(event: ProductEvent.ProductClicked) {
        navigationThrottler.throttle {
            val action = ProductScreenAction.ProductClicked(event.product)
            emitSideEffect(ProductSideEffect.Navigate(action))
        }
    }

    private fun onAddProductToWishlistClicked(event: ProductEvent.AddProductToWishlistClicked) {
        viewModelScope.launch {
            val params = ToggleProductInWishlistUseCase.Params.Product(event.product)
            deps.toggleProductInWishlist(params)
                .onSuccess { isInWishlist ->
                    if (isInWishlist) {
                        val message = ZarinaToastMessage2.productAddedToWishlist(event.product)
                        emitSideEffect(ProductSideEffect.ShowZarinaToast(message))
                    }
                }
                .onFailure(::onToggleProductInWishlistFailure)
        }
    }

    private fun onSizeTableClicked() {
        // TODO: [Top] Implement
    }

    private fun onProductRefreshTriggered() {
        viewModelScope.launch {
            productComponent.fetchProduct()
        }
    }

    private fun onTotalLookProductRefreshTriggered() {
        viewModelScope.launch {
            productComponent.fetchTotalLookProducts()
        }
    }

    private fun onSimilarProductRefreshTriggered() {
        viewModelScope.launch {
            productComponent.fetchSimilarProducts()
        }
    }

    private fun onToggleProductInWishlistFailure(t: Throwable) {
        val message = when (t) {
            is IOException -> ZarinaToastMessage2.NETWORK_ERROR_MESSAGE
            else -> {
                ZarinaToastMessage2(
                    text = Text.Resource(R.string.res_product_adding_to_wishlist_error),
                    startContent = ZarinaToastMessage2.GENERIC_ERROR_DEFAULT_START_ICON,
                )
            }
        }
        emitSideEffect(ProductSideEffect.ShowZarinaToast(message))
    }

    private fun reportScreenCreated() {
        deps.appMetrica.reportScreenOpened(Screen.Product)

        reportScreenCreatedJob?.cancel()
        reportScreenCreatedJob = viewModelScope.launch {
            val product = productComponent.awaitProduct()
            if (product != null) {
                deps.appMetrica.reportProductScreenOpened(product.toAppMetricaProduct())
            }
        }
    }
}
