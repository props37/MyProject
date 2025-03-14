package ru.livetyping.zarina.core.domain.model.product

import java.math.BigDecimal

// Marked as stable on config/compose/stability_config.txt
public data class ProductPrice(
    val originalPrice: BigDecimal,
    val hasDiscount: Boolean,
    val discountPrice: BigDecimal,
    val discountPercent: BigDecimal,
) {
    public constructor(originalPrice: BigDecimal) : this(
        originalPrice = originalPrice,
        hasDiscount = false,
        discountPercent = BigDecimal.ZERO,
        discountPrice = BigDecimal.ZERO,
    )

    val currentPrice: BigDecimal
        get() = if (hasDiscount) discountPrice else originalPrice
}
