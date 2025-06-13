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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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

    private val _sizeSelectorState = MutableStateFlow<SizeSelectorState>(SizeSelectorState.Hidden)
    val sizeSelectorState: StateFlow<SizeSelectorState> = _sizeSelectorState.asStateFlow()

    private val productStateBuilder = ProductState.Builder()

    val productState: StateFlow<ProductState> = combineMore(
        productComponent.productResult,
        productComponent.isProductLoading,
        totalLookProductState,
        similarProductState,
        productComponent.selectedProductSize,
        productComponent.selectedProductHeight,
        productComponent.shouldSelectProductHeight,
    ) { productResult, isProductLoading, totalLookProductState, similarProductState,
        selectedProductSize, selectedProductHeight, shouldSelectProductHeight ->

        productStateBuilder.build(
            productResult = productResult,
            isProductLoading = isProductLoading,
            totalLookProductState = totalLookProductState,
            similarProductState = similarProductState,
            selectedSize = selectedProductSize,
            selectedHeight = selectedProductHeight,
            shouldSelectHeight = shouldSelectProductHeight,
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
            is ProductEvent.AddProductToWishlistClicked -> onAddProductToWishlistClicked(event)
            ProductEvent.SizeTableClicked -> onSizeTableClicked()
            ProductEvent.SelectSizeClicked -> onSelectSizeClicked()
            ProductEvent.SelectHeightClicked -> onSelectHeightClicked()
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

    private fun onSelectSizeClicked() {
        _sizeSelectorState.value = SizeSelectorState.Hidden
        viewModelScope.launch {
            val sizes = productComponent.getProductSizes()
            if (sizes != null) {
                val items = sizes.map {
                    SizeSelectorItem(
                        offer = it,
                        isSelected = it.size == productComponent.selectedProductSize.value,
                    )
                }
                _sizeSelectorState.value = SizeSelectorState.Visible(
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
        _sizeSelectorState.value = SizeSelectorState.Hidden
        viewModelScope.launch {
            val heights = productComponent.getProductHeights()
            if (heights != null) {
                val items = heights.map {
                    SizeSelectorItem(
                        offer = it,
                        isSelected = it.height == productComponent.selectedProductHeight.value,
                    )
                }
                _sizeSelectorState.value = SizeSelectorState.Visible(
                    type = SizeSelectorType.HEIGHT,
                    items = items.toImmutableList(),
                )
            } else {
                val message = ZarinaToastMessage2.genericError()
                emitSideEffect(ProductSideEffect.ShowZarinaToast(message))
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
}
