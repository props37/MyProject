package ru.livetyping.zarina.feature.wishlist.ui.impl.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.analytics.model.Screen
import ru.livetyping.zarina.core.coroutinesutil.WhileUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.combine
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage2
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridSideEffect
import ru.livetyping.zarina.feature.wishlist.ui.impl.R
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model.WishlistEvent
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model.WishlistState
import java.io.IOException
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class WishlistViewModel @Inject constructor(
    private val deps: WishlistDependencies,
) : ViewModel(), SideEffectSource<WishlistSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private var fetchProductIdJob: Job? = null

    private val wishlistProductIdsParams =
        GetWishlistProductIdsFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())

    private val cartProductIdsParams =
        GetCartProductIdsFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())

    private val _productGridSideEffects = Channel<ProductGridSideEffect>(Channel.UNLIMITED)
    val productGridSideEffects: Flow<ProductGridSideEffect> = _productGridSideEffects.receiveAsFlow()

    private val productPagingDataFlow = deps.wishlistProductPager.getWishlistProductPagingDataFlow()
        .cachedIn(viewModelScope)
        .onEach {
            val se = ProductGridSideEffect.ScrollToTop(animate = false)
            _productGridSideEffects.trySend(se)
        }
        .transformProductPagingData()
        .cachedIn(viewModelScope)

    private val getProductIdsUseCaseParams =
        GetWishlistProductIdsFlowUseCase.Params(CachePolicy.LocalOnly)

    private val productCountFlow =
        deps.getWishlistProductIdsFlow(getProductIdsUseCaseParams).map { result ->
            result.getOrNull()?.size
        }

    val wishlistState: StateFlow<WishlistState> = productCountFlow
        .map { productCount ->
            WishlistState(
                productCount = productCount,
                productPagingDataFlow = productPagingDataFlow,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = WishlistState(productCount = null, productPagingDataFlow)
        )

    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event) {
            LifecycleEvent.ON_CREATE -> onScreenCreated()
            LifecycleEvent.ON_START -> onScreenStarted()
            LifecycleEvent.ON_RESUME -> Unit
        }
    }

    fun onWishlistEvent(event: WishlistEvent) {
        when (event) {
            WishlistEvent.BackClicked -> onBackClicked()
            is WishlistEvent.ProductClicked -> onProductClicked(event)
            is WishlistEvent.AddProductToWishlistClicked -> onAddProductToWishlistClicked(event)
            WishlistEvent.RefreshTriggered -> onRefreshTriggered()
            is WishlistEvent.ProductAppendError -> onProductsPaginationError()
        }
    }

    private fun onScreenCreated() {
        deps.appMetrica.reportScreenOpened(Screen.Wishlist)
    }

    private fun onScreenStarted() {
        fetchProductIds()
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = WishlistScreenAction.BackClicked
            emitSideEffect(WishlistSideEffect.Navigate(action))
        }
    }

    private fun onProductClicked(event: WishlistEvent.ProductClicked) {
        navigationThrottler.throttle {
            val action = WishlistScreenAction.ProductClicked(event.product)
            emitSideEffect(WishlistSideEffect.Navigate(action))
        }
    }

    private fun onAddProductToWishlistClicked(event: WishlistEvent.AddProductToWishlistClicked) {
        viewModelScope.launch {
            val product = event.product
            val params = ToggleProductInWishlistUseCase.Params.Product(product)
            deps.toggleProductInWishlist(params)
                .onFailure(::onToggleProductInWishlistFailure)
        }
    }

    private fun onRefreshTriggered() {
        fetchProductIds()
    }

    private fun onProductsPaginationError() {
        val message = ZarinaToastMessage2(
            text = Text.Resource(R.string.wishlist_product_pagination_error),
            startContent = ZarinaToastMessage2.GENERIC_ERROR_DEFAULT_START_ICON,
        )
        emitSideEffect(WishlistSideEffect.ShowZarinaToast(message))
    }

    private fun onToggleProductInWishlistFailure(t: Throwable) {
        val message = when (t) {
            is IOException -> ZarinaToastMessage2.NETWORK_ERROR_MESSAGE
            else -> {
                ZarinaToastMessage2(
                    text = Text.Resource(RCommon.string.res_product_adding_to_wishlist_error),
                    startContent = ZarinaToastMessage2.GENERIC_ERROR_DEFAULT_START_ICON,
                )
            }
        }
        emitSideEffect(WishlistSideEffect.ShowZarinaToast(message))
    }

    private fun fetchProductIds() {
        if (fetchProductIdJob?.isActive == true) return
        fetchProductIdJob = viewModelScope.launch {
            val params = GetWishlistProductIdsFlowUseCase.Params(CachePolicy.Remote())
            deps.getWishlistProductIdsFlow(params).firstOrNull()
        }
    }

    // TODO: [High] Extract?
    private fun Flow<PagingData<ProductShort>>.transformProductPagingData(): Flow<PagingData<ProductShort>> {
        return this.combine(
            deps.getWishlistProductIdsFlow(wishlistProductIdsParams),
            deps.getCartProductIdsFlow(cartProductIdsParams),
        ) { productPagingData, wishlistProductIdsResult, cartProductIdsResult ->
            val wishlistProductIds = wishlistProductIdsResult.getOrDefault(emptySet())
            val cartProductIds = cartProductIdsResult.getOrDefault(emptySet())
            val productIdSet = HashSet<Product.Id>()

            productPagingData
                .filter { product ->
                    productIdSet.add(product.id)
                }
                .map { product ->
                    product.copy(
                        isInWishlist = product.id in wishlistProductIds,
                        isInCart = product.id in cartProductIds,
                    )
                }
        }
    }
}
