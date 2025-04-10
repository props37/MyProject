package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing

internal sealed interface OrderPlacingScreenAction {
    data object BackClicked : OrderPlacingScreenAction
}
