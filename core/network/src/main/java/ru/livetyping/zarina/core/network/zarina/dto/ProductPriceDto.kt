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
        checkPropertyNotNull(commonPrice) { "common_price" }
        checkPropertyNotNull(hasDiscount) { "has_discount" }
        checkPropertyNotNull(discountPrice) { "discount_price" }
        checkPropertyNotNull(discount) { "discount" }
        val discount = if (hasDiscount) {
            ProductPrice.Discount(
                discountPrice = BigDecimal(discountPrice.toDouble()),
                discountPercent = BigDecimal(discount.toDouble()),
            )
        } else null
        return ProductPrice(
            originalPrice = BigDecimal(commonPrice.toDouble()),
            discount = discount,
        )
    }
}
