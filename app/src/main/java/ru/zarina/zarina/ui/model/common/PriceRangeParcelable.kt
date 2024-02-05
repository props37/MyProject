package ru.zarina.zarina.ui.model.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.common.PriceRange

@Parcelize
@Serializable
data class PriceRangeParcelable(
    val min: Long,
    val max: Long,
) : Parcelable {
    fun toPriceRange(): PriceRange = PriceRange(min, max)

    companion object {
        fun from(priceRange: PriceRange): PriceRangeParcelable = PriceRangeParcelable(
            min = priceRange.min,
            max = priceRange.max,
        )
    }
}
