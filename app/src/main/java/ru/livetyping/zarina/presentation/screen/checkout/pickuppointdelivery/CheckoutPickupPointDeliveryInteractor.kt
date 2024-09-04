package ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery

import ru.livetyping.zarina.usecase.checkout.GetPickupPointsFlowUseCase
import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import javax.inject.Inject

class CheckoutPickupPointDeliveryInteractor @Inject constructor(
    val getPickupPointsFlow: GetPickupPointsFlowUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
)
