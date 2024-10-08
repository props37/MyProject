package ru.livetyping.zarina.presentation.screen.product

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductColor
import ru.livetyping.zarina.domain.product.ProductDetails
import ru.livetyping.zarina.domain.product.ProductItem
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.screenresult.ScreenResultHandler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.SizeSelectorGraph
import ru.livetyping.zarina.presentation.screen.product.ProductViewModel.SideEffect
import ru.livetyping.zarina.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.usecase.product.GetProductFlowUseCase
import ru.livetyping.zarina.usecase.product.GetProductSimilarFlowUseCase
import ru.livetyping.zarina.usecase.product.GetProductTotalLookFlowUseCase
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import timber.log.Timber

@HiltViewModel(assistedFactory = ProductViewModel.Factory::class)
class ProductViewModel @AssistedInject constructor(
    @Assisted
    private val sizeSelectorResultFlow: StateFlow<SizeSelectorGraph.Result?>,
    savedStateHandle: SavedStateHandle,
    private val interactor: ProductInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val screenResultHandler = ScreenResultHandler(savedStateHandle)

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val initialProductId: StateFlow<Product.Id> = savedStateHandle
        .getStateFlow<String?>(
            key = UnscopedDestinations.Product.ARG_KEY_PRODUCT_ID,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { value ->
            checkNotNull(value) { "productId is null" }
            Product.Id(value)
        }

    private val productId = MutableStateFlow(initialProductId.value)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val productRequester = FlowRequester(ProductRequest) { request ->
        productId.flatMapLatest { productId ->
            markAsLoading(ProductRequest)
            val params = GetProductFlowUseCase.Params(productId)
            interactor.getProductFlow(params)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val productTotalLookRequester = FlowRequester(ProductRequest) { request ->
        productId.flatMapLatest { productId ->
            markAsLoading(ProductRequest)
            val params = GetProductTotalLookFlowUseCase.Params(productId)
            interactor.getProductTotalLookFlow(params)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val productSimilarRequester = FlowRequester(ProductRequest) { request ->
        productId.flatMapLatest { productId ->
            markAsLoading(ProductRequest)
            val params = GetProductSimilarFlowUseCase.Params(productId)
            interactor.getProductSimilarFlow(params)
        }
    }

    private val productResult: StateFlow<Result<ProductDetails>?> = productRequester.flow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    private val productTotalLookResult: StateFlow<Result<List<ProductItem>>?> =
        productTotalLookRequester.flow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    private val productSimilarResult: StateFlow<Result<List<ProductItem>>?> =
        productSimilarRequester.flow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    val productState: StateFlow<ProductState> = combine(
        productResult,
        productRequester.loadingState,
    ) { productResult, productLoadingState ->
        createProductState(productResult, productLoadingState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = ProductState.Loading,
    )

    val productTotalLookState: StateFlow<SuggestedProductListState> = combine(
        productTotalLookResult,
        productTotalLookRequester.loadingState,
    ) { result, loadingState ->
        createSuggestedProductListState(result, loadingState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = SuggestedProductListState.Loading,
    )

    val productSimilarState: StateFlow<SuggestedProductListState> = combine(
        productSimilarResult,
        productSimilarRequester.loadingState,
    ) { result, loadingState ->
        createSuggestedProductListState(result, loadingState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = SuggestedProductListState.Loading,
    )

    init {
        handleSizeSelectorResult()
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProductScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onShareClicked() {
        val shareUrl = productResult.value?.getOrNull()?.shareUrl ?: return
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.Share(shareUrl.value))
        }
    }

    fun onProductColorClicked(productColor: ProductColor) {
        if (productColor.productId != productId.value) {
            productId.value = productColor.productId
        }
    }

    fun onProductErrorRefreshClicked() {
        productRequester.request(ProductRequest)
        if (productTotalLookResult.value?.isSuccess != true) {
            productTotalLookRequester.request(ProductRequest)
        }
    }

    fun onAddProductToCartClicked(product: Product) {
        if (product.isAvailable) {
            if (product.offers.size > 1) {
                navigationThrottler.throttle {
                    val action = ProductScreenAction.AddProductToCartClicked(product)
                    emitSideEffect(SideEffect.Navigate(action))
                }
            } else {
                val offer = product.offers.firstOrNull() ?: run {
                    Timber.e("Could not add product $product to cart because it has offers")
                    return
                }
                addProductToCart(product.id, offer.barcode)
            }
        } else {
            navigationThrottler.throttle {
                val action = ProductScreenAction.SubscribeToProductClicked(product)
                emitSideEffect(SideEffect.Navigate(action))
            }
        }
    }

    fun onAddProductToFavoritesClicked(product: Product) {
        viewModelScope.launch {
            val params = ToggleProductPresenceInFavoritesUseCase.Params(product.id)
            interactor.toggleProductPresenceInFavorites(params)
                .onSuccess { isProductInFavorites ->
                    if (isProductInFavorites) {
                        val text = Text.Resource(R.string.product_adding_to_favorites_completed)
                        val message = ZarinaToastMessage(text)
                        emitSideEffect(SideEffect.ShowZarinaToast(message))
                    }
                }
                .onFailure {
                    val messageResId = if (product.isInFavorites) {
                        R.string.product_removing_from_favorites_error
                    } else {
                        R.string.product_adding_to_favorites_error
                    }
                    val text = Text.Resource(messageResId)
                    val message = ZarinaToastMessage.error(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
        }
    }

    fun onProductClicked(product: Product) {
        navigationThrottler.throttle {
            val action = ProductScreenAction.ProductClicked(product)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onProductTotalLookErrorRefreshClicked() {
        productTotalLookRequester.request(ProductRequest)
    }

    fun onProductSimilarErrorRefreshClicked() {
        productSimilarRequester.request(ProductRequest)
    }

    fun onUrlClicked(url: Url) {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.OpenUrl(url))
        }
    }

    private fun addProductToCart(productId: Product.Id, barcode: Barcode) {
        viewModelScope.launch {
            val params = AddProductToCartUseCase.Params(
                productId = productId,
                barcode = barcode,
                count = 1,
            )
            interactor.addProductToCart(params)
                .onSuccess {
                    val text = Text.Resource(R.string.product_adding_to_cart_completed)
                    val message = ZarinaToastMessage(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
                .onFailure {
                    val text = Text.Resource(R.string.product_adding_to_cart_error)
                    val message = ZarinaToastMessage.error(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
        }
    }

    private fun handleSizeSelectorResult() {
        viewModelScope.launch {
            screenResultHandler.handle<SizeSelectorGraph.Result>(
                resultFlow = sizeSelectorResultFlow,
                key = KEY_RESULT_SIZE_SELECTOR,
            ) { result ->
                addProductToCart(
                    productId = result.product.toProductItem().id,
                    barcode = result.offer.toProductOffer().barcode,
                )
            }
        }
    }

    private fun createProductState(
        productResult: Result<ProductDetails>?,
        productLoadingState: FlowRequester.LoadingState,
    ): ProductState {
        return if (productResult == null || productLoadingState.isLoading()) {
            ProductState.Loading
        } else {
            productResult.fold(
                onSuccess = { product ->
                    ProductState.Success(product)
                },
                onFailure = {
                    val state = ErrorState.from(it)
                    ProductState.Error(state)
                },
            )
        }
    }

    private fun createSuggestedProductListState(
        productListResult: Result<List<ProductItem>>?,
        productListLoadingState: FlowRequester.LoadingState,
    ): SuggestedProductListState {
        return if (productListResult == null || productListLoadingState.isLoading()) {
            SuggestedProductListState.Loading
        } else {
            productListResult.fold(
                onSuccess = { products ->
                    if (products.isNotEmpty()) {
                        SuggestedProductListState.Success(products.toImmutableList())
                    } else {
                        SuggestedProductListState.Empty
                    }
                },
                onFailure = { SuggestedProductListState.Error },
            )
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ProductScreenAction) : SideEffect

        data class Share(val text: String) : SideEffect

        data class OpenUrl(val url: Url) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    @Stable
    sealed class ProductState {
        @Immutable
        data class Success(val product: ProductDetails) : ProductState()

        data object Loading : ProductState()

        @Immutable
        data class Error(val state: ErrorState) : ProductState()
    }

    @Stable
    sealed class SuggestedProductListState {
        @Immutable
        data class Success(val totalLook: ImmutableList<ProductItem>) : SuggestedProductListState()

        data object Loading : SuggestedProductListState()

        data object Error : SuggestedProductListState()

        data object Empty : SuggestedProductListState()
    }

    private data object ProductRequest : FlowRequester.Request

    @AssistedFactory
    interface Factory {
        fun create(
            sizeSelectorResultFlow: StateFlow<SizeSelectorGraph.Result?>,
        ): ProductViewModel
    }

    companion object {
        private const val KEY_RESULT_SIZE_SELECTOR = "result_size_selector"
    }
}
