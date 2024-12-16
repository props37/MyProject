package ru.livetyping.zarina.feature.profile.ui.impl.impl.order.model

internal sealed interface OrderEvent {
    data object BackClicked : OrderEvent
}
