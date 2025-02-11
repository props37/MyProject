package ru.livetyping.zarina.feature.product.ui.impl.impl

import ru.livetyping.zarina.core.domain.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetProductFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetProductTotalLookFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetSimilarProductsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import javax.inject.Inject

internal class ProductDeps @Inject constructor(
    val getProductFlow: GetProductFlowUseCase,
    val toggleProductInWishlist: ToggleProductInWishlistUseCase,
    val addProductToCart: AddProductToCartUseCase,
    val getProductTotalLookFlow: GetProductTotalLookFlowUseCase,
    val getSimilarProductsFlow: GetSimilarProductsFlowUseCase,
)
