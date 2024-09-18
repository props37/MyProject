package ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector

import ru.livetyping.zarina.domain.checkout.DeliveryOption

sealed class CheckoutCourierDeliveryDateTimeSelectorScreenAction {
    data object ScreenClosed : CheckoutCourierDeliveryDateTimeSelectorScreenAction()

    data class DateTimePeriodSelected(
        val deliveryOptionId: DeliveryOption.Id,
        val selectorType: CourierDeliveryDateTimeSelectorType,
        val dateTimePeriod: DeliveryOption.DateTimePeriod,
    ) : CheckoutCourierDeliveryDateTimeSelectorScreenAction()
}
