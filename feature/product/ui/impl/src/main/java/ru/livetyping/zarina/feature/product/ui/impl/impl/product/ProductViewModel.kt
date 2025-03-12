package ru.livetyping.zarina.feature.product.ui.impl.impl.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetProductFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetProductTotalLookFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetSimilarProductsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uicomponent.sizeselector.viewmodel.SizeSelectorComponent
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.core.uikit.sizeselector.SizeSelectorEvent
import ru.livetyping.zarina.core.uikit.sizeselector.SizeSelectorState
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductState
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductSuggestionsEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductSuggestionsStateBuilder
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.TopBarEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.TopBarState
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: ProductDependencies,
) : ViewModel(), SideEffectSource<ProductSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val sizeSelectorComponent = SizeSelectorComponent(getSizeSelectorComponentListener())

    private val productSuggestionsStateBuilder = ProductSuggestionsStateBuilder()

    private val navEntry = savedStateHandle.toRoute<ProductFeature.NavEntry>()
    private val initialProductId = navEntry.getProductId()

    private val productId = MutableStateFlow(initialProductId)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val productRequester = FlowRequester(ProductRequest) { request ->
        productId.flatMapLatest { productId ->
            markAsLoading(request)
            val params = GetProductFlowUseCase.Params(productId)
            deps.getProductFlow(params)
        }
    }

    private val productResult = productRequester.flow
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val productTotalLookRequester = FlowRequester(ProductRequest) { request ->
        productId.flatMapLatest { productId ->
            markAsLoading(request)
            val params = GetProductTotalLookFlowUseCase.Params(productId)
            deps.getProductTotalLookFlow(params)
        }
    }

    private val productTotalLookResultFlow = productTotalLookRequester.flow
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    private val productTotalLookStateFlow = combine(
        productTotalLookResultFlow,
        productTotalLookRequester.loadingState,
    ) { result, loadingState ->
        productSuggestionsStateBuilder.build(result, loadingState)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val similarProductsRequester = FlowRequester(ProductRequest) { request ->
        productId.flatMapLatest { productId ->
            markAsLoading(request)
            val params = GetSimilarProductsFlowUseCase.Params(productId)
            deps.getSimilarProductsFlow(params)
        }
    }

    private val similarProductsResultFlow = similarProductsRequester.flow
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    private val similarProductsStateFlow = combine(
        similarProductsResultFlow,
        similarProductsRequester.loadingState,
    ) { result, loadingState ->
        productSuggestionsStateBuilder.build(result, loadingState)
    }

    val topBarState: StateFlow<TopBarState> = productResult
        .map { result ->
            val productName = result.getOrNull()?.name
            TopBarState(productName)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
            initialValue = TopBarState(productName = null),
        )

    val productState: StateFlow<ProductState> = combine(
        productResult,
        productRequester.loadingState,
        productTotalLookStateFlow,
        similarProductsStateFlow,
    ) { productResult, productLoadingState, totalLookState, similarProductsState ->
        if (productLoadingState.isLoading()) {
            ProductState.Loading
        } else {
            productResult.fold(
                onSuccess = { product ->
                    val isCheckAvailabilityInStoresButtonVisible =
                        product.offers.any { it.isAvailableInStores }
                    ProductState.Success(
                        product = product,
                        isCheckAvailabilityInStoresButtonVisible = isCheckAvailabilityInStoresButtonVisible,
                        totalLookState = totalLookState,
                        similarProductsState = similarProductsState
                    )
                },
                onFailure = {
                    val errorState = ZarinaErrorScreenState.from(it)
                    ProductState.Error(errorState)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = ProductState.Loading,
    )

    val sizeSelectorState: StateFlow<SizeSelectorState> = sizeSelectorComponent.sizeSelectorState

    fun onTopBarEvent(event: TopBarEvent) {
        when (event) {
            TopBarEvent.BackClicked -> onBackClicked()
            TopBarEvent.ShareClicked -> shareProduct()
        }
    }

    fun onProductEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.ProductColorClicked -> {
                if (event.color.productId != productId.value) {
                    productId.value = event.color.productId
                }
            }

            ProductEvent.CheckAvailabilityInStoresClicked -> onCheckAvailabilityInStoresClicked()
            is ProductEvent.AddToCartClicked -> onAddProductToCartClicked(event)
            is ProductEvent.AddToWishlistClicked -> onAddProductToWishlistClicked(event)
            ProductEvent.ErrorRefreshClicked -> onProductErrorRefreshClicked()
        }
    }

    fun onProductSuggestionsEvent(event: ProductSuggestionsEvent) {
        when (event) {
            is ProductSuggestionsEvent.ProductClicked -> onProductClicked(event.product)
            ProductSuggestionsEvent.ErrorRefreshClicked -> requestProductSuggestionsIfNeeded()
        }
    }

    fun onSizeSelectorEvent(event: SizeSelectorEvent) {
        sizeSelectorComponent.onEvent(event)
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProductScreenAction.BackClicked
            emitSideEffect(ProductSideEffect.Navigate(action))
        }
    }

    private fun shareProduct() {
        viewModelScope.launch {
            val productShareUrl = productResult.firstOrNull()?.getOrNull()?.shareUrl
            if (productShareUrl != null) {
                emitSideEffect(ProductSideEffect.Share(productShareUrl.value))
            }
        }
    }

    private fun onAddProductToWishlistClicked(event: ProductEvent.AddToWishlistClicked) {
        viewModelScope.launch {
            val product = event.product
            val params = ToggleProductInWishlistUseCase.Params(product.id)
            deps.toggleProductInWishlist(params)
                .onSuccess { isInWishlist ->
                    if (isInWishlist) {
                        val text = Text.Resource(RCommon.string.res_product_added_to_wishlist)
                        val message = ZarinaToastMessage(text)
                        emitSideEffect(ProductSideEffect.ShowZarinaToast(message))
                    }
                }
                .onFailure {
                    val messageResId = if (product.isInWishlist) {
                        RCommon.string.res_product_removing_from_wishlist_error
                    } else {
                        RCommon.string.res_product_adding_to_wishlist_error
                    }
                    showZarinaErrorToast(Text.Resource(messageResId))
                }
        }
    }

    private fun onCheckAvailabilityInStoresClicked() {
        navigationThrottler.throttle {
            viewModelScope.launch {
                val product = productResult.firstOrNull()?.getOrNull()
                if (product != null) {
                    val action = ProductScreenAction.CheckAvailabilityInStoresClicked(product)
                    emitSideEffect(ProductSideEffect.Navigate(action))
                }
            }
        }
    }

    private fun onAddProductToCartClicked(event: ProductEvent.AddToCartClicked) {
        val product = event.product
        if (sizeSelectorComponent.shouldShowSizeSelector(product)) {
            sizeSelectorComponent.showSizeSelector(product)
        } else {
            val offer = product.offers.firstOrNull() ?: return
            if (offer.isAvailable) {
                addProductToCart(product, offer)
            } else {
                navigationThrottler.throttle {
                    val action = ProductScreenAction.SubscribeToProductClicked(product, offer)
                    emitSideEffect(ProductSideEffect.Navigate(action))
                }
            }
        }
    }

    private fun addProductToCart(product: Product, offer: ProductOffer) {
        viewModelScope.launch {
            val params = AddProductToCartUseCase.Params(
                productId = product.id,
                barcode = offer.barcode,
                count = 1,
            )
            deps.addProductToCart(params)
                .onSuccess {
                    val text = Text.Resource(RCommon.string.res_product_added_to_cart)
                    val message = ZarinaToastMessage(text)
                    emitSideEffect(ProductSideEffect.ShowZarinaToast(message))
                }
                .onFailure {
                    val text = Text.Resource(RCommon.string.res_product_adding_to_cart_error)
                    showZarinaErrorToast(text)
                }
        }
    }

    private fun onProductClicked(product: Product) {
        navigationThrottler.throttle {
            val action = ProductScreenAction.ProductClicked(product)
            emitSideEffect(ProductSideEffect.Navigate(action))
        }
    }

    private fun onProductErrorRefreshClicked() {
        productRequester.request(ProductRequest)
        requestProductSuggestionsIfNeeded()
    }

    private fun requestProductSuggestionsIfNeeded() {
        viewModelScope.launch {
            val productTotalLookResult = productTotalLookResultFlow.firstOrNull()
            if (productTotalLookResult?.isSuccess != true) {
                productTotalLookRequester.request(ProductRequest)
            }
        }
        viewModelScope.launch {
            val similarProductsResult = similarProductsResultFlow.firstOrNull()
            if (similarProductsResult?.isSuccess != true) {
                similarProductsRequester.request(ProductRequest)
            }
        }
    }

    private fun showZarinaErrorToast(text: Text) {
        val message = ZarinaToastMessage.error(text)
        emitSideEffect(ProductSideEffect.ShowZarinaToast(message))
    }

    private fun getSizeSelectorComponentListener(): SizeSelectorComponent.Listener {
        return object : SizeSelectorComponent.Listener {
            override fun onProductSizeAvailable(product: Product, offer: ProductOffer) {
                addProductToCart(product, offer)
            }

            override fun onProductSizeNotAvailable(product: Product, offer: ProductOffer) {
                val action = ProductScreenAction.SubscribeToProductClicked(product, offer)
                emitSideEffect(ProductSideEffect.Navigate(action))
            }
        }
    }

    private data object ProductRequest : FlowRequest
}
