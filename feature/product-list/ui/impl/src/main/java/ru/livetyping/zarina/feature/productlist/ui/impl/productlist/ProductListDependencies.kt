package ru.livetyping.zarina.feature.productlist.ui.impl.productlist

import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoryPathUseCase
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoryUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import ru.livetyping.zarina.feature.productlist.ui.impl.productlist.paging.ProductPager
import javax.inject.Inject

internal class ProductListDependencies @Inject constructor(
    val productPager: ProductPager,
    val getCategory: GetCategoryUseCase,
    val getWishlistProductIdsFlow: GetWishlistProductIdsFlowUseCase,
    val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
    val toggleProductInWishlist: ToggleProductInWishlistUseCase,
    val getCategoryPath: GetCategoryPathUseCase,
    val appMetrica: AppMetrica,
)
