package ru.zarina.zarina.ui.screen.products

import ru.zarina.zarina.usecase.cart.AddProductToCartUseCase
import ru.zarina.zarina.usecase.cart.GetCartProductIdsFlowUseCase
import ru.zarina.zarina.usecase.category.GetCategoryFlowUseCase
import ru.zarina.zarina.usecase.favorite.GetFavoriteProductIdsFlowUseCase
import ru.zarina.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.zarina.zarina.usecase.product.GetProductPagingDataFlowUseCase
import javax.inject.Inject

class ProductsInteractor @Inject constructor(
    val getCategoryFlow: GetCategoryFlowUseCase,
    val getProductPagingDataFlow: GetProductPagingDataFlowUseCase,
    val getFavoriteProductIdsFlow: GetFavoriteProductIdsFlowUseCase,
    val toggleProductPresenceInFavorites: ToggleProductPresenceInFavoritesUseCase,
    val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
    val addProductToCart: AddProductToCartUseCase,
)
