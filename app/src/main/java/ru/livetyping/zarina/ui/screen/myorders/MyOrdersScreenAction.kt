package ru.livetyping.zarina.ui.screen.myorders

import ru.livetyping.zarina.domain.order.Order

sealed class MyOrdersScreenAction {
    data object ScreenClosed : MyOrdersScreenAction()

    data class OrderClicked(val orderId: Order.Id) : MyOrdersScreenAction()
}
