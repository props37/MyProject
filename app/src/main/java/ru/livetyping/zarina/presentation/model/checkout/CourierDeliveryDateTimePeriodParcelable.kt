package ru.livetyping.zarina.presentation.model.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.checkout.CourierDeliveryOptions

@Parcelize
@Serializable
data class CourierDeliveryDateTimePeriodParcelable(
    val id: Long,
    val date: String,
    val time: String?,
) : Parcelable {

    fun toDateTimePeriod(): CourierDeliveryOptions.Option.DateTimePeriod {
        return CourierDeliveryOptions.Option.DateTimePeriod(
            id = CourierDeliveryOptions.Option.DateTimePeriod.Id(id),
            date = date,
            time = time,
        )
    }

    companion object {
        fun from(
            period: CourierDeliveryOptions.Option.DateTimePeriod,
        ): CourierDeliveryDateTimePeriodParcelable {
            return CourierDeliveryDateTimePeriodParcelable(
                id = period.id.value,
                date = period.date,
                time = period.time,
            )
        }
    }
}
