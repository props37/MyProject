package ru.zarina.zarina.ui.screen.cart

import ru.zarina.zarina.usecase.rework.user.GetUserCityFlowUseCase
import ru.zarina.zarina.usecase.rework.user.SetUserCityUseCase
import javax.inject.Inject

class CartInteractor @Inject constructor(
    val getUserCity: GetUserCityFlowUseCase,
    val setUserCity: SetUserCityUseCase,
)
