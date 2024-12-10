package ru.livetyping.zarina.feature.productlist.ui.impl.impl

import ru.livetyping.zarina.core.domain.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoryFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.paging.ProductPager
import javax.inject.Inject

internal class ProductListDependencies @Inject constructor(
    val productPager: ProductPager,
    val getCategoryFlow: GetCategoryFlowUseCase,
    val getWishlistProductIdsFlow: GetWishlistProductIdsFlowUseCase,
    val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
    val toggleProductInWishlist: ToggleProductInWishlistUseCase,
    val addProductToCart: AddProductToCartUseCase,
)
