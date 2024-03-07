package ru.zarina.zarina.ui.screen.cart

import ru.zarina.zarina.usecase.rework.cart.ClearCartUseCase
import ru.zarina.zarina.usecase.rework.cart.GetCartProductCountFlowUseCase
import ru.zarina.zarina.usecase.rework.user.GetUserCityFlowUseCase
import ru.zarina.zarina.usecase.rework.user.SetUserCityUseCase
import javax.inject.Inject

class CartInteractor @Inject constructor(
    val getCartProductCountFlow: GetCartProductCountFlowUseCase,
    val getUserCity: GetUserCityFlowUseCase,
    val setUserCity: SetUserCityUseCase,
    val clearCart: ClearCartUseCase,
)
