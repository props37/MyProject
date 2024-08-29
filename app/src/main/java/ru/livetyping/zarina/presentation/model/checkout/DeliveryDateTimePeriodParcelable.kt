package ru.livetyping.zarina.presentation.model.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.checkout.DeliveryOptions

@Parcelize
@Serializable
data class DeliveryDateTimePeriodParcelable(
    val id: Long,
    val date: String,
    val time: String?,
) : Parcelable {

    fun toDateTimePeriod(): DeliveryOptions.Option.DateTimePeriod {
        return DeliveryOptions.Option.DateTimePeriod(
            id = DeliveryOptions.Option.DateTimePeriod.Id(id),
            date = date,
            time = time,
        )
    }

    companion object {
        fun from(
            period: DeliveryOptions.Option.DateTimePeriod,
        ): DeliveryDateTimePeriodParcelable {
            return DeliveryDateTimePeriodParcelable(
                id = period.id.value,
                date = period.date,
                time = period.time,
            )
        }
    }
}
