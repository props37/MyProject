package ru.livetyping.zarina.presentation.screen.checkout.recipient

import ru.livetyping.zarina.usecase.checkout.ValidateCustomerUseCase
import ru.livetyping.zarina.usecase.user.GetUserFlowUseCase
import javax.inject.Inject

class CheckoutCustomerInteractor @Inject constructor(
    val getUserFlow: GetUserFlowUseCase,
    val validateCustomer: ValidateCustomerUseCase,
)
