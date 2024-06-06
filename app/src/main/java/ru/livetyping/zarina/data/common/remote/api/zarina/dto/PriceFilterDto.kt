package ru.livetyping.zarina.data.common.remote.api.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.PriceRange
import ru.livetyping.zarina.domain.filter.PriceFilter

@Serializable
data class PriceFilterDto(
    @SerialName("min")
    val min: Int? = null,

    @SerialName("max")
    val max: Int? = null,
) {
    fun toPriceRange(): PriceRange = PriceRange(
        min = checkNotNull(min) { "min is null" },
        max = checkNotNull(max) { "max is null" },
    )

    companion object {
        fun from(priceFilter: PriceFilter): PriceFilterDto? {
            return if (priceFilter.isApplied) {
                PriceFilterDto(
                    min = priceFilter.min,
                    max = priceFilter.max,
                )
            } else null
        }
    }
}
