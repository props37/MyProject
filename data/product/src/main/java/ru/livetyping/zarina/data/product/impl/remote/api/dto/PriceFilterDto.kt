package ru.livetyping.zarina.data.product.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.PriceRange
import ru.livetyping.zarina.core.domain.model.product.filter.ProductPriceFilter
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull

@Serializable
internal data class PriceFilterDto(
    @SerialName("min")
    val min: Int? = null,

    @SerialName("max")
    val max: Int? = null,
) {
    fun toPriceRange(): PriceRange = PriceRange(
        min = checkPropertyNotNull(min) { ::min },
        max = checkPropertyNotNull(max) { ::max },
    )

    companion object {
        fun from(priceFilter: ProductPriceFilter): PriceFilterDto? {
            return if (priceFilter.isApplied) {
                PriceFilterDto(
                    min = priceFilter.min,
                    max = priceFilter.max,
                )
            } else null
        }
    }
}
