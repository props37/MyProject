package ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector

import ru.livetyping.zarina.domain.checkout.CourierDeliveryOptions

sealed class CheckoutCourierDeliveryDateTimeSelectorScreenAction {
    data object ScreenClosed : CheckoutCourierDeliveryDateTimeSelectorScreenAction()

    data class DateTimePeriodSelected(
        val deliveryOptionId: CourierDeliveryOptions.Option.Id,
        val selectorType: CourierDeliveryDateTimeSelectorType,
        val dateTimePeriod: CourierDeliveryOptions.Option.DateTimePeriod,
    ) : CheckoutCourierDeliveryDateTimeSelectorScreenAction()
}
