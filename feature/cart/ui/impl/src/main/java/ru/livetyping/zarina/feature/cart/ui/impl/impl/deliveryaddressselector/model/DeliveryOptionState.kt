package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption

@Immutable
internal data class DeliveryOptionState(
    val deliveryOption: DeliveryOption,
    val isSelected: Boolean,
    val selectedDateTimePeriod: DeliveryOption.DateTimePeriod,
)
