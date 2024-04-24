package ru.livetyping.zarina.ui.screen.favorites

import ru.livetyping.zarina.ui.screen.favorites.paging.FavoriteProductPager
import ru.livetyping.zarina.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.usecase.favorite.ClearFavoriteProductsUseCase
import ru.livetyping.zarina.usecase.favorite.GetFavoriteProductIdsFlowUseCase
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import javax.inject.Inject

class FavoritesInteractor @Inject constructor(
    val favoriteProductPager: FavoriteProductPager,
    val getFavoriteProductIdsFlow: GetFavoriteProductIdsFlowUseCase,
    val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
    val clearFavoriteProducts: ClearFavoriteProductsUseCase,
    val toggleProductPresenceInFavorites: ToggleProductPresenceInFavoritesUseCase,
    val addProductToCart: AddProductToCartUseCase,
)
