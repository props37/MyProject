package ru.livetyping.zarina.core.uimodel.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.ProductPrice

@Serializable
@Parcelize
public data class ProductPriceParcelable(
    val originalPrice: Int,
    val hasDiscount: Boolean,
    val discountPrice: Int,
    val discountPercent: Int,
) : Parcelable {
    public fun toProductPrice(): ProductPrice = ProductPrice(
        originalPrice = originalPrice,
        hasDiscount = hasDiscount,
        discountPrice = discountPrice,
        discountPercent = discountPercent,
    )

    public companion object {
        public fun from(price: ProductPrice): ProductPriceParcelable = ProductPriceParcelable(
            originalPrice = price.originalPrice,
            hasDiscount = price.hasDiscount,
            discountPrice = price.discountPrice,
            discountPercent = price.discountPercent,
        )
    }
}
