package ru.livetyping.zarina.core.domain.model.product

// Marked as stable on config/compose/stability_config.txt
public data class ProductPrice(
    val originalPrice: Int,
    val hasDiscount: Boolean,
    val discountPrice: Int,
    val discountPercent: Int,
) {
    val currentPrice: Int
        get() = if (hasDiscount) discountPrice else originalPrice
}
