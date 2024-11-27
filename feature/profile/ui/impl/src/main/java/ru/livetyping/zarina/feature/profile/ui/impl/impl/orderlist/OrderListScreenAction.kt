package ru.livetyping.zarina.feature.profile.ui.impl.impl.orderlist

internal sealed interface OrderListScreenAction {
    data object ScreenClosed : OrderListScreenAction
}
