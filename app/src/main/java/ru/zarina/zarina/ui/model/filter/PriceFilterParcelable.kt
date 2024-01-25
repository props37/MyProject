package ru.zarina.zarina.ui.model.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.filter.PriceFilter

@Serializable
@Parcelize
data class PriceFilterParcelable(
    val min: Long?,
    val max: Long?,
) : Parcelable {
    companion object {
        fun from(priceFilter: PriceFilter): PriceFilterParcelable = PriceFilterParcelable(
            min = priceFilter.min,
            max = priceFilter.max,
        )
    }
}
