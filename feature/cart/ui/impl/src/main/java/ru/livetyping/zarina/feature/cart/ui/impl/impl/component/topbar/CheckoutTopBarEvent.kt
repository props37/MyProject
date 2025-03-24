package ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar

internal sealed interface CheckoutTopBarEvent {
    data object BackClicked : CheckoutTopBarEvent

    data object CloseClicked : CheckoutTopBarEvent
}
