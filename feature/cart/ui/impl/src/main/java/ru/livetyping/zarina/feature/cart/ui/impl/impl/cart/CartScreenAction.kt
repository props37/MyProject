package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart

internal sealed interface CartScreenAction {
    data object ScreenClosed : CartScreenAction
}
