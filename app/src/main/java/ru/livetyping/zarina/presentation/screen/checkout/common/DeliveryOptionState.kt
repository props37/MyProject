package ru.livetyping.zarina.presentation.screen.checkout.common

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.domain.checkout.DeliveryOption

@Immutable
data class DeliveryOptionState(
    val deliveryOption: DeliveryOption,
    val isSelected: Boolean,
    val selectedDateTimePeriod: DeliveryOption.DateTimePeriod,
)
