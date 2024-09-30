package ru.livetyping.zarina.presentation.screen.checkout.orderplacing

import ru.livetyping.zarina.usecase.checkout.GetCheckoutCartFlowUseCase
import javax.inject.Inject

class CheckoutOrderPlacingInteractor @Inject constructor(
    val getCheckoutCartFlow: GetCheckoutCartFlowUseCase,
)
