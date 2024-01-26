package ru.zarina.zarina.ui.model.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.filter.PriceFilter
import ru.zarina.zarina.ui.model.common.PriceRangeParcelable

@Serializable
@Parcelize
data class PriceFilterParcelable(
    val min: Long?,
    val max: Long?,
    val limits: PriceRangeParcelable,
) : Parcelable {
    fun toPriceFilter(): PriceFilter = PriceFilter(
        min = min,
        max = max,
        limits = limits.toPriceRange(),
    )

    companion object {
        fun from(priceFilter: PriceFilter): PriceFilterParcelable = PriceFilterParcelable(
            min = priceFilter.min,
            max = priceFilter.max,
            limits = PriceRangeParcelable.from(priceFilter.limits),
        )
    }
}
