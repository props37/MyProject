package ru.livetyping.zarina.presentation.screen.checkout.courierdelivery

import ru.livetyping.zarina.domain.checkout.CourierDeliveryOptions

sealed class CheckoutCourierDeliveryScreenAction {
    data object ScreenClosed : CheckoutCourierDeliveryScreenAction()

    data object CheckoutClosed : CheckoutCourierDeliveryScreenAction()

    data class DeliveryDateClicked(
        val dateTimePeriods: List<CourierDeliveryOptions.Option.DateTimePeriod>,
    ) : CheckoutCourierDeliveryScreenAction()

    data class DeliveryTimeClicked(
        val dateTimePeriods: List<CourierDeliveryOptions.Option.DateTimePeriod>,
    ) : CheckoutCourierDeliveryScreenAction()
}
