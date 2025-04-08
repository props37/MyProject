package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector

import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryOptionDateTimeType

internal class DeliveryAddressSelectorNavActions(
    val onBackClicked: () -> Unit,
    val onCloseClicked: () -> Unit,
    val onDeliveryOptionDateTimeClicked: (
        type: DeliveryOptionDateTimeType,
        deliveryOption: DeliveryOption,
        dateTimePeriods: List<DeliveryOption.DateTimePeriod>,
    ) -> Unit,
) : NavigationActions
