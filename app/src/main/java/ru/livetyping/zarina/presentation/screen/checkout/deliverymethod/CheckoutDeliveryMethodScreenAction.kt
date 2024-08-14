package ru.livetyping.zarina.presentation.screen.checkout.deliverymethod

sealed class CheckoutDeliveryMethodScreenAction {
    data object ScreenClosed : CheckoutDeliveryMethodScreenAction()

    data object CheckoutClosed : CheckoutDeliveryMethodScreenAction()
}
