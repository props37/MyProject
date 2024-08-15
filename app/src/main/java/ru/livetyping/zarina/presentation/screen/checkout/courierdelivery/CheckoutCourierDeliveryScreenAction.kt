package ru.livetyping.zarina.presentation.screen.checkout.courierdelivery

sealed class CheckoutCourierDeliveryScreenAction {
    data object ScreenClosed : CheckoutCourierDeliveryScreenAction()

    data object CheckoutClosed : CheckoutCourierDeliveryScreenAction()
}
