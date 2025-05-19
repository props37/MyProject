package ru.livetyping.zarina.core.domain.model.product

import java.math.BigDecimal

// Marked as stable on config/compose/stability_config.txt
public data class ProductPrice(
    val originalPrice: BigDecimal,
    val discount: Discount?,
) {
    val currentPrice: BigDecimal
        get() = discount?.discountPrice ?: originalPrice

    public data class Discount(
        val discountPrice: BigDecimal,
        val discountPercent: BigDecimal,
    )
}
