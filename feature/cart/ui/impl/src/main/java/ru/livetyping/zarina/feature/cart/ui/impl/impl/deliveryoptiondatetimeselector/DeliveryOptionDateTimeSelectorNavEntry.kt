package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.parcelableListNavType
import ru.livetyping.zarina.core.uimodel.checkout.DeliveryOptionParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
internal class DeliveryOptionDateTimeSelectorNavEntry private constructor(
    val type: DeliveryOptionDateTimeSelectorType,
    private val deliveryOptionId: String,
    val dateTimePeriods: List<DeliveryOptionParcelable.DateTimePeriodParcelable>,
) : NavigationEntry {
    fun getDeliveryOptionId(): DeliveryOption.Id = DeliveryOption.Id(deliveryOptionId)

    companion object {
        fun from(
            type: DeliveryOptionDateTimeSelectorType,
            deliveryOption: DeliveryOption,
            dateTimePeriods: List<DeliveryOption.DateTimePeriod>,
        ): DeliveryOptionDateTimeSelectorNavEntry {
            return DeliveryOptionDateTimeSelectorNavEntry(
                type = type,
                deliveryOptionId = deliveryOption.id.value,
                dateTimePeriods = dateTimePeriods.map {
                    DeliveryOptionParcelable.DateTimePeriodParcelable.from(it)
                },
            )
        }

        fun typeMap(): Map<KType, NavType<*>> {
            val typeType = NavType.EnumType(DeliveryOptionDateTimeSelectorType::class.java)
            val dateTimePeriodsType = parcelableListNavType<DeliveryOptionParcelable.DateTimePeriodParcelable>(
                isNullableAllowed = false,
            )
            return mapOf(
                typeOf<DeliveryOptionDateTimeSelectorType>() to typeType,
                typeOf<List<DeliveryOptionParcelable.DateTimePeriodParcelable>>() to dateTimePeriodsType,
            )
        }
    }
}
