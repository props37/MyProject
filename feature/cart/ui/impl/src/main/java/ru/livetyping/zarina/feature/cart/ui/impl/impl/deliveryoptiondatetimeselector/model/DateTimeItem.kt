package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption

@Immutable
internal data class DateTimeItem(
    val dateTimePeriod: DeliveryOption.DateTimePeriod,
    val text: String,
    val isSelected: Boolean,
)
