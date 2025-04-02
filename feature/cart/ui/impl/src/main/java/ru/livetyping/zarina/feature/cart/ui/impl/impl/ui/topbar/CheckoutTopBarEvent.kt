package ru.livetyping.zarina.feature.cart.ui.impl.impl.ui.topbar

internal sealed interface CheckoutTopBarEvent {
    data object BackClicked : CheckoutTopBarEvent

    data object CloseClicked : CheckoutTopBarEvent
}
