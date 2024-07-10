package ru.livetyping.zarina.presentation.screen.cart

import ru.livetyping.zarina.usecase.cart.ApplyMyCardToCartUseCase
import ru.livetyping.zarina.usecase.cart.RemoveMyCardFromCartUseCase
import ru.livetyping.zarina.usecase.cart.ClearCartUseCase
import ru.livetyping.zarina.usecase.cart.FetchCartProductIdsUseCase
import ru.livetyping.zarina.usecase.cart.GetCartFlowUseCase
import ru.livetyping.zarina.usecase.cart.GetCartSizeFlowUseCase
import ru.livetyping.zarina.usecase.cart.RemoveProductFromCartUseCase
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.usecase.user.SetUserCityUseCase
import javax.inject.Inject

class CartInteractor @Inject constructor(
    val fetchCartProductIds: FetchCartProductIdsUseCase,
    val getCartSizeFlow: GetCartSizeFlowUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
    val setUserCity: SetUserCityUseCase,
    val getCartFlow: GetCartFlowUseCase,
    val clearCart: ClearCartUseCase,
    val toggleProductPresenceInFavorites: ToggleProductPresenceInFavoritesUseCase,
    val removeProductFromCart: RemoveProductFromCartUseCase,
    val applyMyCardToCart: ApplyMyCardToCartUseCase,
    val removeMyCardFromCart: RemoveMyCardFromCartUseCase,
)
