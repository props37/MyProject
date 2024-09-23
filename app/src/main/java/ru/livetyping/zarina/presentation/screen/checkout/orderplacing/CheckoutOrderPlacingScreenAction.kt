package ru.livetyping.zarina.presentation.screen.checkout.orderplacing

sealed class CheckoutOrderPlacingScreenAction {
    data object ScreenClosed : CheckoutOrderPlacingScreenAction()

    data object CheckoutClosed : CheckoutOrderPlacingScreenAction()
}
