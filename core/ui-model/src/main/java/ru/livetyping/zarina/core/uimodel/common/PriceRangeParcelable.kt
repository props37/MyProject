package ru.livetyping.zarina.core.uimodel.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.PriceRange

@Serializable
@Parcelize
public data class PriceRangeParcelable(
    val min: Int,
    val max: Int,
) : Parcelable {
    public fun toPriceRange(): PriceRange = PriceRange(min, max)

    public companion object {
        public fun from(priceRange: PriceRange): PriceRangeParcelable = PriceRangeParcelable(
            min = priceRange.min,
            max = priceRange.max,
        )
    }
}
