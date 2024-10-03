package ru.livetyping.zarina.presentation.model.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.checkout.DeliveryOption

@Parcelize
@Serializable
data class DeliveryOptionParcelable(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val dateTimePeriods: List<DateTimePeriod>,
) : Parcelable {

    fun toDeliveryOption(): DeliveryOption {
        return DeliveryOption(
            id = DeliveryOption.Id(id),
            title = title,
            description = description,
            price = price,
            dateTimePeriods = dateTimePeriods.map { it.toDateTimePeriod() },
        )
    }

    @Parcelize
    @Serializable
    data class DateTimePeriod(
        val id: Long,
        val date: String,
        val time: String?,
    ) : Parcelable {

        fun toDateTimePeriod(): DeliveryOption.DateTimePeriod {
            return DeliveryOption.DateTimePeriod(
                id = DeliveryOption.DateTimePeriod.Id(id),
                date = date,
                time = time,
            )
        }

        companion object {
            fun from(
                period: DeliveryOption.DateTimePeriod,
            ): DateTimePeriod {
                return DateTimePeriod(
                    id = period.id.value,
                    date = period.date,
                    time = period.time,
                )
            }
        }
    }

    companion object {
        fun from(deliveryOption: DeliveryOption): DeliveryOptionParcelable {
            return DeliveryOptionParcelable(
                id = deliveryOption.id.value,
                title = deliveryOption.title,
                description = deliveryOption.description,
                price = deliveryOption.price,
                dateTimePeriods = deliveryOption.dateTimePeriods.map {
                    DateTimePeriod.from(it)
                },
            )
        }
    }
}
