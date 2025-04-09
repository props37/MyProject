package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector

import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class DeliveryOptionDateTimeSelectorNavActions(
    val onBackClicked: () -> Unit,
    val onDateTimePeriodSelected: (
        selectorType: DeliveryOptionDateTimeSelectorType,
        deliveryOptionId: DeliveryOption.Id,
        dateTimePeriod: DeliveryOption.DateTimePeriod,
    ) -> Unit,
) : NavigationActions
