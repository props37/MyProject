package ru.livetyping.zarina.presentation.screen.products

import ru.livetyping.zarina.presentation.screen.products.paging.ProductPager
import ru.livetyping.zarina.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.usecase.category.GetCategoryFlowUseCase
import ru.livetyping.zarina.usecase.category.GetCategoryPathUseCase
import ru.livetyping.zarina.usecase.favorite.GetFavoriteProductIdsFlowUseCase
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import javax.inject.Inject

class ProductsInteractor @Inject constructor(
    val getCategoryFlow: GetCategoryFlowUseCase,
    val productPager: ProductPager,
    val getFavoriteProductIdsFlow: GetFavoriteProductIdsFlowUseCase,
    val toggleProductPresenceInFavorites: ToggleProductPresenceInFavoritesUseCase,
    val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
    val addProductToCart: AddProductToCartUseCase,
    val getCategoryPath: GetCategoryPathUseCase,
)
