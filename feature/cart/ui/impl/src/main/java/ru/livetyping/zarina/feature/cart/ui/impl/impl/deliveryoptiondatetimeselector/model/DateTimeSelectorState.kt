package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.DeliveryOptionDateTimeSelectorType

@Immutable
internal data class DateTimeSelectorState(
    val selectorType: DeliveryOptionDateTimeSelectorType,
    val dateTimeItems: ImmutableList<DateTimeItem>,
    val isContinueButtonVisible: Boolean,
)
