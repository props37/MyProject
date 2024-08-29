package ru.livetyping.zarina.presentation.screen.checkout.postdelivery

import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import javax.inject.Inject

class CheckoutPostDeliveryInteractor @Inject constructor(
    val getUserCityFlow: GetUserCityFlowUseCase,
)
