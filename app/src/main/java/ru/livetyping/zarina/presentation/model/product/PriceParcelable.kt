package ru.livetyping.zarina.presentation.model.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.product.Price

@Serializable
@Parcelize
data class PriceParcelable(
    val originalPrice: Int,
    val hasDiscount: Boolean,
    val discountPrice: Int,
    val discountPercent: Int,
) : Parcelable {
    fun toPrice(): Price = Price(
        originalPrice = originalPrice,
        hasDiscount = hasDiscount,
        discountPrice = discountPrice,
        discountPercent = discountPercent,
    )

    companion object {
        fun from(price: Price): PriceParcelable = PriceParcelable(
            originalPrice = price.originalPrice,
            hasDiscount = price.hasDiscount,
            discountPrice = price.discountPrice,
            discountPercent = price.discountPercent,
        )
    }
}
