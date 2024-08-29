package ru.livetyping.zarina.presentation.screen.checkout.courierdelivery

import ru.livetyping.zarina.domain.checkout.DeliveryOptions

sealed class CheckoutCourierDeliveryScreenAction {
    data object ScreenClosed : CheckoutCourierDeliveryScreenAction()

    data object CheckoutClosed : CheckoutCourierDeliveryScreenAction()

    data class DeliveryDateClicked(
        val deliveryOptionId: DeliveryOptions.Option.Id,
        val dateTimePeriods: List<DeliveryOptions.Option.DateTimePeriod>,
    ) : CheckoutCourierDeliveryScreenAction()

    data class DeliveryTimeClicked(
        val deliveryOptionId: DeliveryOptions.Option.Id,
        val dateTimePeriods: List<DeliveryOptions.Option.DateTimePeriod>,
    ) : CheckoutCourierDeliveryScreenAction()
}
