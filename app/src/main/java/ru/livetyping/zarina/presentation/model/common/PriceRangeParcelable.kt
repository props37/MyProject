package ru.livetyping.zarina.presentation.model.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.PriceRange

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
