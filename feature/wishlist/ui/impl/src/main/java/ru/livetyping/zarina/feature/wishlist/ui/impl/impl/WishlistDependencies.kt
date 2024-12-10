package ru.livetyping.zarina.feature.wishlist.ui.impl.impl

import ru.livetyping.zarina.core.domain.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ClearWishlistUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.paging.WishlistProductPager
import javax.inject.Inject

internal class WishlistDependencies @Inject constructor(
    val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
    val getWishlistProductIdsFlow: GetWishlistProductIdsFlowUseCase,
    val wishlistProductPager: WishlistProductPager,
    val toggleProductInWishlist: ToggleProductInWishlistUseCase,
    val addProductToCart: AddProductToCartUseCase,
    val clearWishlist: ClearWishlistUseCase,
)
