package ru.zarina.zarina.ui.screen.cart

import ru.zarina.zarina.usecase.cart.ClearCartUseCase
import ru.zarina.zarina.usecase.cart.GetCartFlowUseCase
import ru.zarina.zarina.usecase.cart.GetCartSizeFlowUseCase
import ru.zarina.zarina.usecase.user.GetUserCityFlowUseCase
import ru.zarina.zarina.usecase.user.SetUserCityUseCase
import javax.inject.Inject

class CartInteractor @Inject constructor(
    val getCartSizeFlow: GetCartSizeFlowUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
    val setUserCity: SetUserCityUseCase,
    val getCartFlow: GetCartFlowUseCase,
    val clearCart: ClearCartUseCase,
)
