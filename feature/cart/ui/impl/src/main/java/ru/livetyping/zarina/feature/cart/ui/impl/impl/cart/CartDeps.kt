package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart

import ru.livetyping.zarina.core.domain.usecase.cart.ClearCartUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductCountFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SetUserCityUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import javax.inject.Inject

internal class CartDeps @Inject constructor(
    val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
    val getCartProductCountFlow: GetCartProductCountFlowUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
    val setUserCity: SetUserCityUseCase,
    val getCartFlow: GetCartFlowUseCase,
    val clearCart: ClearCartUseCase,
    val toggleProductInWishlist: ToggleProductInWishlistUseCase,
    // TODO: [Top] Add RemoveProductFromCartUseCase
    // TODO: [Top] Add ApplyMyCardToCartUseCase
    // TODO: [Top] Add ApplyPromoCodeUseCase
    // TODO: [Top] Add RemovePromoCodeUseCase
    // TODO: [Top] Add ApplyBonusWriteOffUseCase
    // TODO: [Top] Add RemoveBonusWriteOffUseCase
)
