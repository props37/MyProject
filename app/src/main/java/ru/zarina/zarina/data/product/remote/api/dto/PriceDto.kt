package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Price
import ru.zarina.zarina.utils.kotlin.isNotNull

@Serializable
data class PriceDto(
    @SerialName("common_price")
    val regularPrice: Int? = null,
    @SerialName("has_discount")
    val isDiscount: Boolean? = null,
    @SerialName("discount")
    val discountPercentage: Int? = null,
    @SerialName("discount_price")
    val discountedPrice: Int? = null,
) {

    fun toDomain(): Price? {
        return if (
            isNotNull(discountedPrice, "discount_price")
            && isNotNull(regularPrice, "common_price")
        )
            Price(
                current = discountedPrice,
                original = regularPrice,
            )
        else
            null
    }

}
