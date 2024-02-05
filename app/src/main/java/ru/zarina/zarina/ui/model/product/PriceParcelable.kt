package ru.zarina.zarina.ui.model.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.zarina.zarina.domain.rework.product.Price

@Parcelize
data class PriceParcelable(
    val originalPrice: Long,
    val hasDiscount: Boolean,
    val discountPrice: Long,
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
