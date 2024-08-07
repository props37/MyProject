package ru.livetyping.zarina.presentation.screen.orderplacement.recipient

sealed class OrderPlacementRecipientScreenAction {
    data object OrderPlacementClosed : OrderPlacementRecipientScreenAction()
}
