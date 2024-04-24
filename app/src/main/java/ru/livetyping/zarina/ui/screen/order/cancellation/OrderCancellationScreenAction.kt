package ru.livetyping.zarina.ui.screen.order.cancellation

sealed class OrderCancellationScreenAction {
    data object ScreenClosed : OrderCancellationScreenAction()

    data object OrderCancelled : OrderCancellationScreenAction()
}
