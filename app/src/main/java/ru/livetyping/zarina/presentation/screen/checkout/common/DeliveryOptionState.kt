package ru.livetyping.zarina.presentation.screen.checkout.common

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.domain.checkout.DeliveryOptions

@Immutable
data class DeliveryOptionState(
    val deliveryOption: DeliveryOptions.Option,
    val isSelected: Boolean,
    val selectedDateTimePeriod: DeliveryOptions.Option.DateTimePeriod,
)
