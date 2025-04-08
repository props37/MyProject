package ru.livetyping.zarina.core.uimodel.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import java.math.BigDecimal

@Parcelize
@Serializable
public data class DeliveryOptionParcelable(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val dateTimePeriods: List<DateTimePeriodParcelable>,
) : Parcelable {
    public fun toDeliveryOption(): DeliveryOption {
        return DeliveryOption(
            id = DeliveryOption.Id(id),
            title = title,
            description = description,
            price = BigDecimal(price),
            dateTimePeriods = dateTimePeriods.map { it.toDateTimePeriod() },
        )
    }

    @Parcelize
    @Serializable
    public data class DateTimePeriodParcelable(
        val id: String,
        val date: String,
        val time: String?,
    ) : Parcelable {
        public fun toDateTimePeriod(): DeliveryOption.DateTimePeriod {
            return DeliveryOption.DateTimePeriod(
                id = DeliveryOption.DateTimePeriod.Id(id),
                date = date,
                time = time,
            )
        }

        public companion object {
            public fun from(period: DeliveryOption.DateTimePeriod): DateTimePeriodParcelable {
                return DateTimePeriodParcelable(
                    id = period.id.value,
                    date = period.date,
                    time = period.time,
                )
            }
        }
    }

    public companion object {
        public fun from(deliveryOption: DeliveryOption): DeliveryOptionParcelable {
            return DeliveryOptionParcelable(
                id = deliveryOption.id.value,
                title = deliveryOption.title,
                description = deliveryOption.description,
                price = deliveryOption.price.toString(),
                dateTimePeriods = deliveryOption.dateTimePeriods.map {
                    DateTimePeriodParcelable.from(it)
                },
            )
        }
    }
}
