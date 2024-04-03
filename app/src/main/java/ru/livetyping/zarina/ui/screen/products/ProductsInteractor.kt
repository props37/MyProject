package ru.livetyping.zarina.ui.screen.products

import ru.livetyping.zarina.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.usecase.category.GetCategoryFlowUseCase
import ru.livetyping.zarina.usecase.favorite.GetFavoriteProductIdsFlowUseCase
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.usecase.product.GetProductPagingDataFlowUseCase
import javax.inject.Inject

class ProductsInteractor @Inject constructor(
    val getCategoryFlow: GetCategoryFlowUseCase,
    val getProductPagingDataFlow: GetProductPagingDataFlowUseCase,
    val getFavoriteProductIdsFlow: GetFavoriteProductIdsFlowUseCase,
    val toggleProductPresenceInFavorites: ToggleProductPresenceInFavoritesUseCase,
    val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
    val addProductToCart: AddProductToCartUseCase,
)
