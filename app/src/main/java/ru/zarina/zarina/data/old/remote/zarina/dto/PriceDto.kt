package ru.zarina.zarina.data.old.remote.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.old.ApiContract
import ru.zarina.zarina.domain.Price

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
            ApiContract.isNotNull(discountedPrice, "discount_price")
            && ApiContract.isNotNull(regularPrice, "common_price")
        )
            Price(
                current = discountedPrice,
                original = regularPrice,
            )
        else
            null
    }

}
