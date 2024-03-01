package ru.zarina.zarina.ui.screen.cart

import ru.zarina.zarina.usecase.rework.user.GetUserCityFlowUseCase
import javax.inject.Inject

class CartInteractor @Inject constructor(
    val getUserCity: GetUserCityFlowUseCase,
)
