package ru.livetyping.zarina.feature.product.ui.impl.impl.product

import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.domain.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetProductAiReviewsUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetProductTotalLookUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetProductUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetSimilarProductsUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import javax.inject.Inject

internal class ProductDependencies @Inject constructor(
    val getProduct: GetProductUseCase,
    val getProductTotalLook: GetProductTotalLookUseCase,
    val getSimilarProducts: GetSimilarProductsUseCase,
    val addProductToCart: AddProductToCartUseCase,
    val toggleProductInWishlist: ToggleProductInWishlistUseCase,
    val getWishlistProductIdsFlow: GetWishlistProductIdsFlowUseCase,
    val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
    val appMetrica: AppMetrica,
    val getProductAiReviewsUseCase: GetProductAiReviewsUseCase,
)
