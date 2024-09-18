package ru.livetyping.zarina.presentation.model.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.checkout.DeliveryOption

@Parcelize
@Serializable
data class DeliveryDateTimePeriodParcelable(
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
        ): DeliveryDateTimePeriodParcelable {
            return DeliveryDateTimePeriodParcelable(
                id = period.id.value,
                date = period.date,
                time = period.time,
            )
        }
    }
}
