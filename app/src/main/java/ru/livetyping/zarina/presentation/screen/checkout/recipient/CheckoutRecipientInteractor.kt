package ru.livetyping.zarina.presentation.screen.checkout.recipient

import ru.livetyping.zarina.usecase.checkout.ValidateRecipientUseCase
import ru.livetyping.zarina.usecase.user.GetUserFlowUseCase
import javax.inject.Inject

class CheckoutRecipientInteractor @Inject constructor(
    val getUserFlow: GetUserFlowUseCase,
    val validateRecipient: ValidateRecipientUseCase,
)
