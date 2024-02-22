package ru.zarina.zarina.ui.screen.products

import ru.zarina.zarina.usecase.rework.cart.AddProductToCartUseCase
import ru.zarina.zarina.usecase.rework.cart.GetCartProductIdsFlowUseCase
import ru.zarina.zarina.usecase.rework.category.GetCategoryFlowUseCase
import ru.zarina.zarina.usecase.rework.favorite.AddProductToFavoritesUseCase
import ru.zarina.zarina.usecase.rework.favorite.GetFavoriteProductIdsFlowUseCase
import ru.zarina.zarina.usecase.rework.favorite.RemoveProductFromFavoritesUseCase
import ru.zarina.zarina.usecase.rework.product.GetProductPagingDataFlowUseCase
import javax.inject.Inject

class ProductsInteractor @Inject constructor(
    val getCategoryFlow: GetCategoryFlowUseCase,
    val getProductPagingDataFlow: GetProductPagingDataFlowUseCase,
    val getFavoriteProductIdsFlow: GetFavoriteProductIdsFlowUseCase,
    val addProductToFavorites: AddProductToFavoritesUseCase,
    val removeProductFromFavorites: RemoveProductFromFavoritesUseCase,
    val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
    val addProductToCart: AddProductToCartUseCase,
)
