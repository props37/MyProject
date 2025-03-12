package ru.livetyping.zarina.feature.wishlist.ui.impl.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.combine
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uicomponent.sizeselector.viewmodel.SizeSelectorComponent
import ru.livetyping.zarina.core.uikit.sizeselector.SizeSelectorEvent
import ru.livetyping.zarina.core.uikit.sizeselector.SizeSelectorState
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridSideEffect
import ru.livetyping.zarina.feature.wishlist.ui.impl.R
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model.TopBarEvent
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model.TopBarState
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model.WishlistEvent
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class WishlistViewModel @Inject constructor(
    private val deps: WishlistDependencies,
) : ViewModel(), SideEffectSource<WishlistSideEffect> by SideEffectSourceImpl() {

    // TODO: [Medium] Inject dispatcher
    private val viewModelScopeDefault = viewModelScope + Dispatchers.Default

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private val sizeSelectorComponent = SizeSelectorComponent(getSizeSelectorComponentListener())

    private var clearWishlistJob: Job? = null

    private val localWishlistProductIdsParams = GetWishlistProductIdsFlowUseCase.Params(
        cachePolicy = CachePolicy.LocalOnly,
    )
    private val localWishlistProductIdsResultFlow =
        deps.getWishlistProductIdsFlow(localWishlistProductIdsParams)

    val topBarState: StateFlow<TopBarState> = combine(
        localWishlistProductIdsResultFlow,
        operationTracker.isOperationOngoing(ClearWishlistOperation),
    ) { wishlistProductIdsResult, isWishlistClearingOngoing ->
        val wishlistProductIds = wishlistProductIdsResult.getOrNull()
        val isClearButtonVisible = !wishlistProductIds.isNullOrEmpty()
        TopBarState(
            isClearButtonVisible = isClearButtonVisible,
            isClearButtonLoading = isWishlistClearingOngoing,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = TopBarState.getInitial(),
    )

    private val wishlistProductsRequester = FlowRequester<PagingData<ProductShort>, WishlistProductsRequest>(
        coroutineScope = viewModelScope,
    ) {
        deps.wishlistProductPager.getWishlistProductPagingDataFlow()
    }

    private val cartProductIdsParams =
        GetCartProductIdsFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())
    private val cartProductIdsResultFlow = deps.getCartProductIdsFlow(cartProductIdsParams)

    private val _productGridSideEffects = Channel<ProductGridSideEffect>(Channel.UNLIMITED)
    val productGridSideEffects: Flow<ProductGridSideEffect> = _productGridSideEffects.receiveAsFlow()

    val productPagingDataFlow: Flow<PagingData<ProductShort>> = wishlistProductsRequester.flow
        .cachedIn(viewModelScopeDefault)
        .onEach { _productGridSideEffects.trySend(ProductGridSideEffect.ScrollToTop) }
        .combine(
            localWishlistProductIdsResultFlow,
            cartProductIdsResultFlow,
        ) { productPagingData, wishlistProductIdsResult, cartProductIdsResult ->
            val wishlistProductIds = wishlistProductIdsResult.getOrDefault(emptySet())
            val cartProductIds = cartProductIdsResult.getOrDefault(emptySet())
            productPagingData.map { product ->
                product.copy(
                    isInWishlist = product.id in wishlistProductIds,
                    isInCart = product.id in cartProductIds,
                )
            }
        }
        .cachedIn(viewModelScopeDefault)

    val sizeSelectorState: StateFlow<SizeSelectorState> = sizeSelectorComponent.sizeSelectorState

    fun onTopBarEvent(event: TopBarEvent) {
        when (event) {
            TopBarEvent.ClearWishlistClicked -> onClearWishlistClicked()
        }
    }

    fun onWishlistEvent(event: WishlistEvent) {
        when (event) {
            is WishlistEvent.ProductClicked -> onProductClicked(event)
            is WishlistEvent.AddToWishlistClicked -> onAddProductToWishlistClicked(event)
            is WishlistEvent.AddToCartClicked -> onAddProductToCartClicked(event)
            is WishlistEvent.SubscribeClicked -> onSubscribeToProductClicked(event)
            WishlistEvent.GoToCatalogClicked -> onGoToCatalogClicked()
        }
    }

    fun onSizeSelectorEvent(event: SizeSelectorEvent) {
        sizeSelectorComponent.onEvent(event)
    }

    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event) {
            LifecycleEvent.ON_CREATE -> onScreenCreated()
            LifecycleEvent.ON_START -> onScreenStarted()
            LifecycleEvent.ON_RESUME -> Unit
        }
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = WishlistScreenAction.BackClicked
            emitSideEffect(WishlistSideEffect.Navigate(action))
        }
    }

    private fun onClearWishlistClicked() {
        if (clearWishlistJob?.isActive == true) return

        clearWishlistJob = viewModelScope.launch {
            operationTracker.track(ClearWishlistOperation) {
                deps.clearWishlist()
                    .onSuccess {
                        wishlistProductsRequester.request(WishlistProductsRequest)
                    }
                    .onFailure {
                        val text = Text.Resource(R.string.wishlist_clearing_error)
                        val message = ZarinaToastMessage.error(text)
                        emitSideEffect(WishlistSideEffect.ShowZarinaToast(message))
                    }
            }
        }
    }

    private fun onProductClicked(event: WishlistEvent.ProductClicked) {
        navigationThrottler.throttle {
            val action = WishlistScreenAction.ProductClicked(event.product)
            emitSideEffect(WishlistSideEffect.Navigate(action))
        }
    }

    private fun onAddProductToWishlistClicked(event: WishlistEvent.AddToWishlistClicked) {
        viewModelScope.launch {
            val product = event.product
            val params = ToggleProductInWishlistUseCase.Params(product.id)
            deps.toggleProductInWishlist(params)
                .onSuccess { isInWishlist ->
                    if (isInWishlist) {
                        val text = Text.Resource(ru.livetyping.zarina.core.resource.R.string.res_product_added_to_wishlist)
                        val message = ZarinaToastMessage(text)
                        emitSideEffect(WishlistSideEffect.ShowZarinaToast(message))
                    }
                }
                .onFailure {
                    val textResId = if (product.isInWishlist) {
                        RCommon.string.res_product_removing_from_wishlist_error
                    } else {
                        RCommon.string.res_product_adding_to_wishlist_error
                    }
                    val text = Text.Resource(textResId)
                    showZarinaErrorToast(text)
                }
        }
    }

    private fun onAddProductToCartClicked(event: WishlistEvent.AddToCartClicked) {
        val product = event.product
        if (sizeSelectorComponent.shouldShowSizeSelector(product)) {
            sizeSelectorComponent.showSizeSelector(product)
        } else {
            val offer = product.offers.firstOrNull() ?: return
            if (offer.isAvailable) {
                addProductToCart(product, offer)
            } else {
                navigationThrottler.throttle {
                    val action = WishlistScreenAction.SubscribeToProductClicked(product, offer)
                    emitSideEffect(WishlistSideEffect.Navigate(action))
                }
            }
        }
    }

    private fun onSubscribeToProductClicked(event: WishlistEvent.SubscribeClicked) {
        val product = event.product
        if (sizeSelectorComponent.shouldShowSizeSelector(product)) {
            sizeSelectorComponent.showSizeSelector(product)
        } else {
            navigationThrottler.throttle {
                val offer = product.offers.firstOrNull() ?: return@throttle
                val action = WishlistScreenAction.SubscribeToProductClicked(product, offer)
                emitSideEffect(WishlistSideEffect.Navigate(action))
            }
        }
    }

    private fun onGoToCatalogClicked() {
        navigationThrottler.throttle {
            val action = WishlistScreenAction.GoToCatalogClicked
            emitSideEffect(WishlistSideEffect.Navigate(action))
        }
    }

    private fun onScreenCreated() {
        wishlistProductsRequester.request(WishlistProductsRequest)
    }

    private fun onScreenStarted() {
        viewModelScope.launch {
            val params = GetWishlistProductIdsFlowUseCase.Params(CachePolicy.Remote())
            deps.getWishlistProductIdsFlow(params).firstOrNull()
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
                    emitSideEffect(WishlistSideEffect.ShowZarinaToast(message))
                }
                .onFailure {
                    val text = Text.Resource(RCommon.string.res_product_adding_to_cart_error)
                    showZarinaErrorToast(text)
                }
        }
    }

    private fun showZarinaErrorToast(text: Text) {
        val message = ZarinaToastMessage.error(text)
        emitSideEffect(WishlistSideEffect.ShowZarinaToast(message))
    }

    private fun getSizeSelectorComponentListener(): SizeSelectorComponent.Listener {
        return object : SizeSelectorComponent.Listener {
            override fun onProductSizeAvailable(product: Product, offer: ProductOffer) {
                addProductToCart(product, offer)
            }

            override fun onProductSizeNotAvailable(product: Product, offer: ProductOffer) {
                val action = WishlistScreenAction.SubscribeToProductClicked(product, offer)
                emitSideEffect(WishlistSideEffect.Navigate(action))
            }
        }
    }

    private data object WishlistProductsRequest : FlowRequest

    private data object ClearWishlistOperation : OperationKey
}
