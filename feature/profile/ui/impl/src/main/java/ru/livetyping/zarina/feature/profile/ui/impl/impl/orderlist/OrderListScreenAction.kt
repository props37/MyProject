package ru.livetyping.zarina.feature.profile.ui.impl.impl.orderlist

import ru.livetyping.zarina.core.domain.model.order.Order

internal sealed interface OrderListScreenAction {
    data object BackClicked : OrderListScreenAction

    data class OrderClicked(val order: Order) : OrderListScreenAction
}
