package ru.livetyping.zarina.data.common.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.PriceRange
import ru.livetyping.zarina.domain.filter.PriceFilter

@Serializable
data class PriceFilterDto(
    @SerialName("min")
    val min: Long? = null,

    @SerialName("max")
    val max: Long? = null,
) {
    fun toPriceRange(): PriceRange = PriceRange(
        min = checkNotNull(min) { "min is null" },
        max = checkNotNull(max) { "max is null" },
    )

    companion object {
        fun from(priceFilter: PriceFilter): PriceFilterDto? {
            return if (!priceFilter.isEmpty) {
                PriceFilterDto(
                    min = priceFilter.min,
                    max = priceFilter.max,
                )
            } else null
        }
    }
}
