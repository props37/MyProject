package ru.livetyping.zarina.presentation.screen.checkout.deliverymethod

import ru.livetyping.zarina.usecase.checkout.GetDeliveryMethodsFlowUseCase
import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import javax.inject.Inject

class CheckoutDeliveryMethodInteractor @Inject constructor(
    val getDeliveryMethodsFlow: GetDeliveryMethodsFlowUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
)
