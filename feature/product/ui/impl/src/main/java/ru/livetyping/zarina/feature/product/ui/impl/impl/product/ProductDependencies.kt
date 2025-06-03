package ru.livetyping.zarina.feature.product.ui.impl.impl.product

import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.domain.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetProductUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetProductTotalLookFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetSimilarProductsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import javax.inject.Inject

internal class ProductDependencies @Inject constructor(
    val getProduct: GetProductUseCase,
    val toggleProductInWishlist: ToggleProductInWishlistUseCase,
    val addProductToCart: AddProductToCartUseCase,
    val getProductTotalLookFlow: GetProductTotalLookFlowUseCase,
    val getSimilarProductsFlow: GetSimilarProductsFlowUseCase,
    val appMetrica: AppMetrica,
)
