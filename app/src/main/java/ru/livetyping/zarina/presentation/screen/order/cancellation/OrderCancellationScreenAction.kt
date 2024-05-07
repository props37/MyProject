package ru.livetyping.zarina.presentation.screen.order.cancellation

sealed class OrderCancellationScreenAction {
    data object ScreenClosed : OrderCancellationScreenAction()

    data object OrderCancelled : OrderCancellationScreenAction()
}
