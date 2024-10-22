package ru.livetyping.zarina.feature.wishlist.ui.impl.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.plus
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.usecase.wishlist.FetchWishlistProductIdsUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.throttler.Throttler
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.paging.WishlistProductPager
import javax.inject.Inject

@HiltViewModel
internal class WishlistViewModel @Inject constructor(
    private val wishlistProductPager: WishlistProductPager,
    private val fetchWishlistProductIds: FetchWishlistProductIdsUseCase,
    private val toggleProductInWishlist: ToggleProductInWishlistUseCase,
) : ViewModel(), SideEffectSource<WishlistSideEffect> by SideEffectSourceImpl() {

    private val viewModelScopeDefault = viewModelScope + Dispatchers.Default

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val wishlistProductsRequester = FlowRequester(WishlistProductsRequest) {
        wishlistProductPager.getWishlistProductPagingDataFlow()
    }

    // TODO: [Top] Update product state
    val productPagingDataFlow: Flow<PagingData<ProductShort>> = wishlistProductsRequester.flow
        .cachedIn(viewModelScopeDefault)

    private data object WishlistProductsRequest : FlowRequest
}
