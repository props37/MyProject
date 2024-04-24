package ru.livetyping.zarina.ui.screen.order

import ru.livetyping.zarina.domain.order.Order

sealed class OrderScreenAction {
    data object ScreenClosed : OrderScreenAction()

    data class CancelOrderClicked(val orderId: Order.Id) : OrderScreenAction()
}
