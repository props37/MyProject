package ru.livetyping.zarina.presentation.screen.checkout.storeselection

sealed class CheckoutStoreSelectionScreenAction {
    data object ScreenClosed : CheckoutStoreSelectionScreenAction()

    data object CheckoutClosed : CheckoutStoreSelectionScreenAction()
}
