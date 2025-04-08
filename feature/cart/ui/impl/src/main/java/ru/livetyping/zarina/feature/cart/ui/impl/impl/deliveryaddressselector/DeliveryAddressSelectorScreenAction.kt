package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector

import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryOptionDateTimeType

internal sealed interface DeliveryAddressSelectorScreenAction {
    data object BackClicked : DeliveryAddressSelectorScreenAction

    data object CloseClicked : DeliveryAddressSelectorScreenAction

    data class SelectDeliveryOptionDateTimeClicked(
        val type: DeliveryOptionDateTimeType,
        val deliveryOption: DeliveryOption,
        val dateTimePeriods: List<DeliveryOption.DateTimePeriod>,
    ) : DeliveryAddressSelectorScreenAction
}
