package ru.livetyping.zarina.feature.wishlist.ui.impl.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.usecase.wishlist.FetchWishlistProductIdsUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import ru.livetyping.zarina.core.paging.updateProducts
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.throttler.Throttler
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.component.TopBarEvent
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.component.TopBarState
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.paging.WishlistProductPager
import javax.inject.Inject

@HiltViewModel
internal class WishlistViewModel @Inject constructor(
    getWishlistProductIdsFlow: GetWishlistProductIdsFlowUseCase,
    private val wishlistProductPager: WishlistProductPager,
    private val fetchWishlistProductIds: FetchWishlistProductIdsUseCase,
    private val toggleProductInWishlist: ToggleProductInWishlistUseCase,
) : ViewModel(), SideEffectSource<WishlistSideEffect> by SideEffectSourceImpl() {

    private val viewModelScopeDefault = viewModelScope + Dispatchers.Default

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val topBarState: StateFlow<TopBarState> = getWishlistProductIdsFlow()
        .map { result ->
            val wishlistProductIds = result.getOrNull()
            val isClearButtonVisible = !wishlistProductIds.isNullOrEmpty()
            TopBarState(isClearButtonVisible)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
            initialValue = TopBarState(isClearButtonVisible = false),
        )

    private val wishlistProductsRequester = FlowRequester<PagingData<ProductShort>, WishlistProductsRequest> {
        wishlistProductPager.getWishlistProductPagingDataFlow()
    }

    val productPagingDataFlow: Flow<PagingData<ProductShort>> = wishlistProductsRequester.flow
        .cachedIn(viewModelScopeDefault)
        .updateProducts(
            wishlistProductIdsFlow = emptyFlow(), // TODO: [Top] Implement
            cartProductIdsFlow = emptyFlow(), // TODO: [Top] Implement
        )
        .cachedIn(viewModelScopeDefault)

    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event) {
            LifecycleEvent.ON_CREATE -> onScreenOpened()
            LifecycleEvent.ON_START -> onScreenStarted()
            LifecycleEvent.ON_RESUME -> Unit
        }
    }

    fun onTopBarEvent(event: TopBarEvent) {
        when (event) {
            TopBarEvent.ClearClicked -> clearWishlist()
        }
    }

    private fun onScreenOpened() {
        wishlistProductsRequester.request(WishlistProductsRequest)
    }

    private fun onScreenStarted() {
        viewModelScope.launch {
            fetchWishlistProductIds()
        }
    }

    private fun clearWishlist() {
        // TODO: [Top] Implement
    }

    private data object WishlistProductsRequest : FlowRequest
}
