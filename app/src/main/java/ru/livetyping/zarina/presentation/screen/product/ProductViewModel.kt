package ru.livetyping.zarina.presentation.screen.product

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductColor
import ru.livetyping.zarina.domain.product.ProductDetails
import ru.livetyping.zarina.domain.product.ProductItem
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.datafetchinginfo.DataFetchingInfoHolder
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.screen.product.ProductViewModel.SideEffect
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.usecase.product.GetProductFlowUseCase
import ru.livetyping.zarina.usecase.product.GetProductSimilarFlowUseCase
import ru.livetyping.zarina.usecase.product.GetProductTotalLookFlowUseCase
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ProductInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

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

    private val productFetchingInfoHolder = DataFetchingInfoHolder<Unit>()
    private val productTotalLookFetchingInfoHolder = DataFetchingInfoHolder<Unit>()
    private val productSimilarFetchingInfoHolder = DataFetchingInfoHolder<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val productResult: StateFlow<Result<ProductDetails>?> = combine(
        productFetchingInfoHolder.fetchingRequests,
        productId,
    ) { _, productId ->
        val params = GetProductFlowUseCase.Params(productId)
        interactor.getProductFlow(params)
    }
        .flatMapLatest { it }
        .onEach { productFetchingInfoHolder.completeFetching() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val productTotalLookResult: StateFlow<Result<List<ProductItem>>?> = combine(
        productTotalLookFetchingInfoHolder.fetchingRequests,
        productId,
    ) { _, productId ->
        val params = GetProductTotalLookFlowUseCase.Params(productId)
        interactor.getProductTotalLookFlow(params)
    }
        .flatMapLatest { it }
        .onEach { productTotalLookFetchingInfoHolder.completeFetching() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val productSimilarResult: StateFlow<Result<List<ProductItem>>?> = combine(
        productSimilarFetchingInfoHolder.fetchingRequests,
        productId,
    ) { _, productId ->
        val params = GetProductSimilarFlowUseCase.Params(productId)
        interactor.getProductSimilarFlow(params)
    }
        .flatMapLatest { it }
        .onEach { productSimilarFetchingInfoHolder.completeFetching() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val productState: StateFlow<ProductState> = productResult.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { result ->
        result?.fold(
            onSuccess = { product ->
                ProductState.Success(product)
            },
            onFailure = {
                val state = ErrorState.from(it)
                ProductState.Error(state)
            },
        ) ?: ProductState.Loading
    }

    val productTotalLookState: StateFlow<SuggestedProductListState> = productTotalLookResult.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { result ->
        result?.fold(
            onSuccess = { totalLook ->
                if (totalLook.isNotEmpty()) {
                    SuggestedProductListState.Success(totalLook.toImmutableList())
                } else {
                    SuggestedProductListState.Empty
                }
            },
            onFailure = { SuggestedProductListState.Error },
        ) ?: SuggestedProductListState.Loading
    }

    val productSimilarState: StateFlow<SuggestedProductListState> = productSimilarResult.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { result ->
        result?.fold(
            onSuccess = { totalLook ->
                if (totalLook.isNotEmpty()) {
                    SuggestedProductListState.Success(totalLook.toImmutableList())
                } else {
                    SuggestedProductListState.Empty
                }
            },
            onFailure = { SuggestedProductListState.Error },
        ) ?: SuggestedProductListState.Loading
    }

    init {
        productFetchingInfoHolder.requestFetching(Unit)
        productTotalLookFetchingInfoHolder.requestFetching(Unit)
        productSimilarFetchingInfoHolder.requestFetching(Unit)
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
        productFetchingInfoHolder.requestFetching(Unit)
        if (productTotalLookResult.value?.isSuccess != true) {
            productTotalLookFetchingInfoHolder.requestFetching(Unit)
        }
    }

    fun onAddProductToCartClicked(product: Product) {
        // TODO: [High] Implement
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
        productTotalLookFetchingInfoHolder.requestFetching(Unit)
    }

    fun onProductSimilarErrorRefreshClicked() {
        productSimilarFetchingInfoHolder.requestFetching(Unit)
    }

    fun onUrlClicked(url: Url) {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.OpenUrl(url))
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
}
