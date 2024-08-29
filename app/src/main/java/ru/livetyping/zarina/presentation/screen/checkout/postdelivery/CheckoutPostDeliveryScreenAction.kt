package ru.livetyping.zarina.presentation.screen.checkout.postdelivery

sealed class CheckoutPostDeliveryScreenAction {
    data object ScreenClosed : CheckoutPostDeliveryScreenAction()

    data object CheckoutClosed : CheckoutPostDeliveryScreenAction()
}
