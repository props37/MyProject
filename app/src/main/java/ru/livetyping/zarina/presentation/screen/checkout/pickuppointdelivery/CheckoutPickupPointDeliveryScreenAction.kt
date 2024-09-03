package ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery

sealed class CheckoutPickupPointDeliveryScreenAction {
    data object ScreenClosed : CheckoutPickupPointDeliveryScreenAction()

    data object CheckoutClosed : CheckoutPickupPointDeliveryScreenAction()
}
