package ru.livetyping.zarina.feature.profile.ui.impl.order.model

internal sealed interface OrderCancellationDialogEvent {
    data object CloseClicked : OrderCancellationDialogEvent

    data object CancelOrderClicked : OrderCancellationDialogEvent
}
