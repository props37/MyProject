package ru.livetyping.zarina.core.network.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.ProductPrice
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import java.math.BigDecimal

@Serializable
public data class ProductPriceDto(
    @SerialName("common_price")
    val commonPrice: Float? = null,

    @SerialName("has_discount")
    val hasDiscount: Boolean? = null,

    @SerialName("discount")
    val discount: Float? = null,

    @SerialName("discount_price")
    val discountPrice: Float? = null,
) {
    public fun toProductPrice(): ProductPrice {
        checkPropertyNotNull(commonPrice) { ::commonPrice }
        checkPropertyNotNull(hasDiscount) { ::hasDiscount }
        checkPropertyNotNull(discountPrice) { ::discountPrice }
        checkPropertyNotNull(discount) { ::discount }
        return ProductPrice(
            originalPrice = BigDecimal(commonPrice.toDouble()),
            hasDiscount = hasDiscount,
            discountPrice = BigDecimal(discountPrice.toDouble()),
            discountPercent = BigDecimal(discount.toDouble()),
        )
    }
}
