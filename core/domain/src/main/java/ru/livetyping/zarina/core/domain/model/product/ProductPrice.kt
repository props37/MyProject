package ru.livetyping.zarina.core.domain.model.product

public data class ProductPrice(
    val originalPrice: Int,
    val hasDiscount: Boolean,
    val discountPrice: Int,
    val discountPercent: Int,
) {
    val currentPrice: Int
        get() = if (hasDiscount) discountPrice else originalPrice
}
