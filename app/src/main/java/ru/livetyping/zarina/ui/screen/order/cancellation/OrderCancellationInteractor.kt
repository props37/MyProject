package ru.livetyping.zarina.ui.screen.order.cancellation

import ru.livetyping.zarina.usecase.order.CancelOrderUseCase
import javax.inject.Inject

class OrderCancellationInteractor @Inject constructor(
    val cancelOrder: CancelOrderUseCase,
)
