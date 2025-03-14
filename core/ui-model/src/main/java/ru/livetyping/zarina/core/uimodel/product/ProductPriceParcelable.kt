package ru.livetyping.zarina.core.uimodel.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.ProductPrice
import java.math.BigDecimal

@Serializable
@Parcelize
public data class ProductPriceParcelable(
    val originalPrice: String,
    val hasDiscount: Boolean,
    val discountPrice: String,
    val discountPercent: String,
) : Parcelable {
    public fun toProductPrice(): ProductPrice = ProductPrice(
        originalPrice = BigDecimal(originalPrice),
        hasDiscount = hasDiscount,
        discountPrice = BigDecimal(discountPrice),
        discountPercent = BigDecimal(discountPercent),
    )

    public companion object {
        public fun from(price: ProductPrice): ProductPriceParcelable = ProductPriceParcelable(
            originalPrice = price.originalPrice.toString(),
            hasDiscount = price.hasDiscount,
            discountPrice = price.discountPrice.toString(),
            discountPercent = price.discountPercent.toString(),
        )
    }
}
