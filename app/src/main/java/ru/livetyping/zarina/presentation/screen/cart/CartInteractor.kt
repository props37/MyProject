package ru.livetyping.zarina.presentation.screen.cart

import ru.livetyping.zarina.usecase.cart.ApplyBonusWriteOffUseCase
import ru.livetyping.zarina.usecase.cart.ApplyMyCardToCartUseCase
import ru.livetyping.zarina.usecase.cart.ApplyPromoCodeUseCase
import ru.livetyping.zarina.usecase.cart.ClearCartUseCase
import ru.livetyping.zarina.usecase.cart.FetchCartProductIdsUseCase
import ru.livetyping.zarina.usecase.cart.GetCartFlowUseCase
import ru.livetyping.zarina.usecase.cart.GetCartProductCountFlowUseCase
import ru.livetyping.zarina.usecase.cart.RemoveBonusWriteOffUseCase
import ru.livetyping.zarina.usecase.cart.RemoveMyCardFromCartUseCase
import ru.livetyping.zarina.usecase.cart.RemoveProductFromCartUseCase
import ru.livetyping.zarina.usecase.cart.RemovePromoCodeUseCase
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.usecase.user.FetchUserCityUseCase
import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.usecase.user.SetUserCityUseCase
import javax.inject.Inject

class CartInteractor @Inject constructor(
    val fetchCartProductIds: FetchCartProductIdsUseCase,
    val getCartProductCountFlow: GetCartProductCountFlowUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
    val setUserCity: SetUserCityUseCase,
    val getCartFlow: GetCartFlowUseCase,
    val clearCart: ClearCartUseCase,
    val toggleProductPresenceInFavorites: ToggleProductPresenceInFavoritesUseCase,
    val removeProductFromCart: RemoveProductFromCartUseCase,
    val applyMyCardToCart: ApplyMyCardToCartUseCase,
    val removeMyCardFromCart: RemoveMyCardFromCartUseCase,
    val applyPromoCode: ApplyPromoCodeUseCase,
    val removePromoCode: RemovePromoCodeUseCase,
    val applyBonusWriteOff: ApplyBonusWriteOffUseCase,
    val removeBonusWriteOff: RemoveBonusWriteOffUseCase,
    val fetchUserCity: FetchUserCityUseCase,
)
