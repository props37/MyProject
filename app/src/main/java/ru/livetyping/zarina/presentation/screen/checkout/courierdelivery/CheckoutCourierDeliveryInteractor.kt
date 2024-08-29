package ru.livetyping.zarina.presentation.screen.checkout.courierdelivery

import ru.livetyping.zarina.usecase.checkout.GetCourierDeliveryOptionsFlowUseCase
import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import javax.inject.Inject

class CheckoutCourierDeliveryInteractor @Inject constructor(
    val getUserCityFlow: GetUserCityFlowUseCase,
    val getCourierDeliveryOptionsFlow: GetCourierDeliveryOptionsFlowUseCase,
)
