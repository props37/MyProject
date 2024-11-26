package ru.livetyping.zarina.core.network.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.ProductPrice
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull

@Serializable
public data class ProductPriceDto(
    @SerialName("common_price")
    val commonPrice: Int? = null,

    @SerialName("has_discount")
    val hasDiscount: Boolean? = null,

    @SerialName("discount")
    val discount: Int? = null,

    @SerialName("discount_price")
    val discountPrice: Int? = null,
) {
    public fun toProductPrice(): ProductPrice {
        return ProductPrice(
            originalPrice = checkPropertyNotNull(commonPrice) { ::commonPrice },
            hasDiscount = checkPropertyNotNull(hasDiscount) { ::hasDiscount },
            discountPrice = checkPropertyNotNull(discountPrice) { ::discountPrice },
            discountPercent = checkPropertyNotNull(discount) { ::discount },
        )
    }
}
