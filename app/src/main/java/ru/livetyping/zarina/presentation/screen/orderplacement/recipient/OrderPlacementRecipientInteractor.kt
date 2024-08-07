package ru.livetyping.zarina.presentation.screen.orderplacement.recipient

import ru.livetyping.zarina.usecase.user.GetUserFlowUseCase
import javax.inject.Inject

class OrderPlacementRecipientInteractor @Inject constructor(
    val getUserFlow: GetUserFlowUseCase,
)
