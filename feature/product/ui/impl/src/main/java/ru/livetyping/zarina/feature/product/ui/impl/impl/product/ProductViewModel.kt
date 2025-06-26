package ru.livetyping.zarina.feature.product.ui.impl.impl.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.analytics.model.Screen
import ru.livetyping.zarina.core.coroutinesutil.WhileUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.combineMore
import ru.livetyping.zarina.core.domain.analytics.toAppMetricaProduct
import ru.livetyping.zarina.core.domain.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage2
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.component.ProductComponent
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductActionButtonState
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductState
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.SizeSelectorItem
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.SizeSelectorState
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.SizeSelectorType
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.SuggestionListState
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
internal class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: ProductDependencies,
) : ViewModel(), SideEffectSource<ProductSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var addProductToCartJob: Job? = null

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

    private val suggestionListStateBuilder = SuggestionListState.Builder()

    private val totalLookProductState = combine(
        productComponent.totalLookProductsResult,
        productComponent.areTotalLookProductsLoading,
    ) { totalLookProductsResult, areTotalLookProductsLoading ->
        suggestionListStateBuilder.build(totalLookProductsResult, areTotalLookProductsLoading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SuggestionListState.Loading,
    )

    private val similarProductState = combine(
        productComponent.similarProductsResult,
        productComponent.areSimilarProductsLoading,
    ) { similarProductsResult, areSimilarProductsLoading ->
        suggestionListStateBuilder.build(similarProductsResult, areSimilarProductsLoading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SuggestionListState.Loading,
    )

    private val productActionButtonState = combine(
        productComponent.productResult,
        productComponent.selectedProductOffer,
        operationTracker.ongoingOperationKeys,
    ) { productResult, selectedProductOffer, ongoingOperations ->
        val product = productResult?.getOrNull()
        val isAddingToCartInProgress = AddProductToCartOperation in ongoingOperations
        when {
            product == null -> ProductActionButtonState.AddToCart(isAddingToCartInProgress)
            product.isInCart -> ProductActionButtonState.InCart(isAddingToCartInProgress)
            selectedProductOffer?.isAvailable != true -> ProductActionButtonState.NotifyWhenAvailable
            else -> ProductActionButtonState.AddToCart(isAddingToCartInProgress)
        }
    }

    private val sizeSelectorState = MutableStateFlow<SizeSelectorState>(SizeSelectorState.Hidden)

    private val productStateBuilder = ProductState.Builder()

    val productState: StateFlow<ProductState> = combineMore(
        productComponent.productResult,
        productComponent.isProductLoading,
        totalLookProductState,
        similarProductState,
        productComponent.selectedProductSize,
        productComponent.selectedProductHeight,
        productComponent.shouldSelectProductHeight,
        productActionButtonState,
        sizeSelectorState,
    ) { productResult, isProductLoading, totalLookProductState, similarProductState,
        selectedProductSize, selectedProductHeight, shouldSelectProductHeight, productActionButtonState,
        sizeSelectorState ->

        productStateBuilder.build(
            productResult = productResult,
            isProductLoading = isProductLoading,
            totalLookProductState = totalLookProductState,
            similarProductState = similarProductState,
            selectedSize = selectedProductSize,
            selectedHeight = selectedProductHeight,
            shouldSelectHeight = shouldSelectProductHeight,
            productActionButtonState = productActionButtonState,
            sizeSelectorState = sizeSelectorState,
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

    fun onProductEvent(event: ProductEvent) {
        when (event) {
            ProductEvent.BackClicked -> onBackClicked()
            ProductEvent.ShareClicked -> onShareClicked()
            is ProductEvent.ProductColorClicked -> onProductColorClicked(event)
            is ProductEvent.ProductClicked -> onProductClicked(event)
            is ProductEvent.CheckAvailabilityInStoresClicked -> {
                onCheckAvailabilityInStoresClicked(event)
            }

            is ProductEvent.AddProductToCartClicked -> onAddProductToCartClicked(event)
            is ProductEvent.SubscribeToProductClicked -> onSubscribeToProductClicked(event)
            is ProductEvent.AddProductToWishlistClicked -> onAddProductToWishlistClicked(event)
            ProductEvent.SizeTableClicked -> onSizeTableClicked()
            ProductEvent.SelectSizeClicked -> onSelectSizeClicked()
            ProductEvent.SelectHeightClicked -> onSelectHeightClicked()
            is ProductEvent.SizeSelected -> onSizeSelected(event)
            ProductEvent.ProductRefreshTriggered -> onProductRefreshTriggered()
            ProductEvent.TotalLookProductRefreshTriggered -> onTotalLookProductRefreshTriggered()
            ProductEvent.SimilarProductRefreshTriggered -> onSimilarProductRefreshTriggered()
            ProductEvent.SizeSelectorDismissed -> onSizeSelectorDismissed()
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
        if (productComponent.setProductId(event.color.productId)) {
            reportScreenCreated()
        }
    }

    private fun onProductClicked(event: ProductEvent.ProductClicked) {
        navigationThrottler.throttle {
            val action = ProductScreenAction.ProductClicked(event.product)
            emitSideEffect(ProductSideEffect.Navigate(action))
        }
    }

    private fun onCheckAvailabilityInStoresClicked(event: ProductEvent.CheckAvailabilityInStoresClicked) {
        navigationThrottler.throttle {
            val action = ProductScreenAction.CheckAvailabilityInStoresClicked(event.product)
            emitSideEffect(ProductSideEffect.Navigate(action))
        }
    }

    private fun onAddProductToCartClicked(event: ProductEvent.AddProductToCartClicked) {
        if (addProductToCartJob?.isActive == true) return
        addProductToCartJob = viewModelScope.launch {
            operationTracker.track(AddProductToCartOperation) {
                val offer = productComponent.selectedProductOffer.firstOrNull()
                if (offer != null) {
                    val params =
                        AddProductToCartUseCase.Params(event.product, offer.barcode, count = 1)
                    deps.addProductToCart(params)
                        .onSuccess {
                            val message = ZarinaToastMessage2.productAddedToCart(event.product)
                            emitSideEffect(ProductSideEffect.ShowZarinaToast(message))
                        }
                        .onFailure(::onAddProductToCartFailure)
                }
            }
        }
    }

    private fun onSubscribeToProductClicked(event: ProductEvent.SubscribeToProductClicked) {
        navigationThrottler.throttle {
            viewModelScope.launch {
                val offer = productComponent.selectedProductOffer.firstOrNull()
                if (offer != null) {
                    val action = ProductScreenAction.SubscribeToProductClicked(event.product, offer)
                    emitSideEffect(ProductSideEffect.Navigate(action))
                }
            }
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
        navigationThrottler.throttle {
            viewModelScope.launch {
                val product = productComponent.awaitProduct()
                val measurements = product?.measurements
                val sizeGuide = product?.sizeGuide
                if (measurements != null && sizeGuide != null) {
                    val action = ProductScreenAction.SizeTableClicked(measurements, sizeGuide)
                    emitSideEffect(ProductSideEffect.Navigate(action))
                }
            }
        }
    }

    private fun onSelectSizeClicked() {
        sizeSelectorState.value = SizeSelectorState.Hidden
        viewModelScope.launch {
            val sizes = productComponent.getProductSizes()
            if (sizes != null) {
                val items = sizes.map {
                    SizeSelectorItem(
                        offer = it,
                        isSelected = it.size == productComponent.selectedProductSize.value,
                    )
                }
                sizeSelectorState.value = SizeSelectorState.Visible(
                    type = SizeSelectorType.SIZE,
                    items = items.toImmutableList(),
                )
            } else {
                val message = ZarinaToastMessage2.genericError()
                emitSideEffect(ProductSideEffect.ShowZarinaToast(message))
            }
        }
    }

    private fun onSelectHeightClicked() {
        sizeSelectorState.value = SizeSelectorState.Hidden
        viewModelScope.launch {
            val heights = productComponent.getProductHeights()
            if (heights != null) {
                val items = heights.map {
                    SizeSelectorItem(
                        offer = it,
                        isSelected = it.height == productComponent.selectedProductHeight.value,
                    )
                }
                sizeSelectorState.value = SizeSelectorState.Visible(
                    type = SizeSelectorType.HEIGHT,
                    items = items.toImmutableList(),
                )
            } else {
                val message = ZarinaToastMessage2.genericError()
                emitSideEffect(ProductSideEffect.ShowZarinaToast(message))
            }
        }
    }

    private fun onSizeSelected(event: ProductEvent.SizeSelected) {
        sizeSelectorState.value = SizeSelectorState.Hidden
        when (event.type) {
            SizeSelectorType.SIZE -> {
                productComponent.setSelectedProductSize(event.offer.size)
            }

            SizeSelectorType.HEIGHT -> {
                event.offer.height?.let {
                    productComponent.setSelectedProductHeight(it)
                }
            }
        }
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

    private fun onSizeSelectorDismissed() {
        sizeSelectorState.value = SizeSelectorState.Hidden
    }

    private fun onAddProductToCartFailure(t: Throwable) {
        val message = when (t) {
            is IOException -> ZarinaToastMessage2.networkError()
            else -> {
                ZarinaToastMessage2(
                    text = Text.Resource(R.string.res_product_adding_to_cart_error),
                    startContent = ZarinaToastMessage2.StartContent.Icon.genericError(),
                )
            }
        }
        emitSideEffect(ProductSideEffect.ShowZarinaToast(message))
    }

    private fun onToggleProductInWishlistFailure(t: Throwable) {
        val message = when (t) {
            is IOException -> ZarinaToastMessage2.networkError()
            else -> {
                ZarinaToastMessage2(
                    text = Text.Resource(R.string.res_product_adding_to_wishlist_error),
                    startContent = ZarinaToastMessage2.StartContent.Icon.genericError(),
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

    private data object AddProductToCartOperation : OperationKey
}
