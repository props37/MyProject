package ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector

import ru.livetyping.zarina.domain.checkout.DeliveryOptions

sealed class CheckoutCourierDeliveryDateTimeSelectorScreenAction {
    data object ScreenClosed : CheckoutCourierDeliveryDateTimeSelectorScreenAction()

    data class DateTimePeriodSelected(
        val deliveryOptionId: DeliveryOptions.Option.Id,
        val selectorType: CourierDeliveryDateTimeSelectorType,
        val dateTimePeriod: DeliveryOptions.Option.DateTimePeriod,
    ) : CheckoutCourierDeliveryDateTimeSelectorScreenAction()
}
