package ru.livetyping.zarina.feature.wishlist.ui.impl.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.usecase.wishlist.ClearWishlistUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import ru.livetyping.zarina.core.paging.updateProducts
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.feature.wishlist.ui.impl.R
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model.TopBarEvent
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model.TopBarState
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model.WishlistEvent
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.paging.WishlistProductPager
import javax.inject.Inject

@HiltViewModel
internal class WishlistViewModel @Inject constructor(
    private val getWishlistProductIdsFlow: GetWishlistProductIdsFlowUseCase,
    private val wishlistProductPager: WishlistProductPager,
    private val toggleProductInWishlist: ToggleProductInWishlistUseCase,
    private val clearWishlist: ClearWishlistUseCase,
) : ViewModel(), SideEffectSource<WishlistSideEffect> by SideEffectSourceImpl() {

    // TODO: [High] Inject dispatcher
    private val viewModelScopeDefault = viewModelScope + Dispatchers.Default

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var clearWishlistJob: Job? = null

    private val wishlistProductIdsParams = GetWishlistProductIdsFlowUseCase.Params(
        cachePolicy = CachePolicy.LocalOnly,
    )

    val topBarState: StateFlow<TopBarState> = combine(
        getWishlistProductIdsFlow(wishlistProductIdsParams),
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

    private val wishlistProductsRequester = FlowRequester<PagingData<ProductShort>, WishlistProductsRequest> {
        wishlistProductPager.getWishlistProductPagingDataFlow()
    }

    val productPagingDataFlow: Flow<PagingData<ProductShort>> = wishlistProductsRequester.flow
        .cachedIn(viewModelScopeDefault)
        .updateProducts(
            wishlistProductIdsFlow = flowOf(emptySet()), // TODO: [Top] Implement
            cartProductIdsFlow = flowOf(emptySet()), // TODO: [Top] Implement
        )
        .cachedIn(viewModelScopeDefault)

    fun onTopBarEvent(event: TopBarEvent) {
        when (event) {
            TopBarEvent.ClearWishlistClicked -> onClearWishlistClicked()
        }
    }

    // TODO: [Top] Implement
    fun onWishlistEvent(event: WishlistEvent) {
        when (event) {
            is WishlistEvent.AddToCartClicked -> TODO()
            is WishlistEvent.AddToFavoritesClicked -> TODO()
            is WishlistEvent.ProductClicked -> TODO()
            is WishlistEvent.SubscribeClicked -> TODO()
            WishlistEvent.GoToCatalogClicked -> onGoToCatalogClicked()
        }
    }

    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event) {
            LifecycleEvent.ON_CREATE -> onScreenCreated()
            LifecycleEvent.ON_START -> onScreenStarted()
            LifecycleEvent.ON_RESUME -> Unit
        }
    }

    private fun onClearWishlistClicked() {
        if (clearWishlistJob?.isActive == true) return

        clearWishlistJob = viewModelScope.launch {
            operationTracker.track(ClearWishlistOperation) {
                clearWishlist()
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
            getWishlistProductIdsFlow(params).firstOrNull()
        }
    }

    private data object WishlistProductsRequest : FlowRequest

    private data object ClearWishlistOperation : OperationKey
}
