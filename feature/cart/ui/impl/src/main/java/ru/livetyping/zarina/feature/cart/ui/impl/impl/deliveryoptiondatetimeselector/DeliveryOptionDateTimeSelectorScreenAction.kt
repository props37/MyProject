package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector

import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption

internal sealed interface DeliveryOptionDateTimeSelectorScreenAction {
    data object BackClicked : DeliveryOptionDateTimeSelectorScreenAction

    data class DateTimePeriodSelected(
        val selectorType: DeliveryOptionDateTimeSelectorType,
        val deliveryOptionId: DeliveryOption.Id,
        val dateTimePeriod: DeliveryOption.DateTimePeriod,
    ) : DeliveryOptionDateTimeSelectorScreenAction
}
