package ru.livetyping.zarina.data.common.remote.api.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.product.Price

@Serializable
data class PriceDto(
    @SerialName("common_price")
    val originalPrice: Int? = null,

    @SerialName("has_discount")
    val hasDiscount: Boolean? = null,

    @SerialName("discount")
    val discountPercent: Int? = null,

    @SerialName("discount_price")
    val discountPrice: Int? = null,
) {
    fun toPrice(): Price {
        return Price(
            originalPrice = checkNotNull(originalPrice) { "commonPrice is null" },
            hasDiscount = checkNotNull(hasDiscount) { "hasDiscount is null" },
            discountPrice = checkNotNull(discountPrice) { "discountPrice is null" },
            discountPercent = checkNotNull(discountPercent) { "discountPercent is null" },
        )
    }
}
