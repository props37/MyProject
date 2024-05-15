package ru.livetyping.zarina.presentation.screen.order

import ru.livetyping.zarina.usecase.order.GetOrderFlowUseCase
import javax.inject.Inject

class OrderInteractor @Inject constructor(
    val getOrderFlow: GetOrderFlowUseCase,
)
