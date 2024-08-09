package ru.livetyping.zarina.presentation.screen.orderplacement.storeselection

sealed class OrderPlacementStoreSelectionScreenAction {
    data object ScreenClosed : OrderPlacementStoreSelectionScreenAction()

    data object OrderPlacementClosed : OrderPlacementStoreSelectionScreenAction()
}
