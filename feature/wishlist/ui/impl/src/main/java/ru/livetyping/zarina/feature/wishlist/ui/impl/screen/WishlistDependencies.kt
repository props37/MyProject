package ru.livetyping.zarina.feature.wishlist.ui.impl.screen

import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import ru.livetyping.zarina.feature.wishlist.ui.impl.screen.paging.WishlistProductPager
import javax.inject.Inject

internal class WishlistDependencies @Inject constructor(
    val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
    val getWishlistProductIdsFlow: GetWishlistProductIdsFlowUseCase,
    val wishlistProductPager: WishlistProductPager,
    val toggleProductInWishlist: ToggleProductInWishlistUseCase,
    val appMetrica: AppMetrica,
)
