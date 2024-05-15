package ru.livetyping.zarina.domain.product

data class Price(
    val originalPrice: Int,
    val hasDiscount: Boolean,
    val discountPrice: Int,
    val discountPercent: Int,
) {
    constructor(originalPrice: Int) : this(
        originalPrice = originalPrice,
        hasDiscount = false,
        discountPercent = 0,
        discountPrice = 0,
    )
}

val Price.currentPrice: Int
    get() = if (hasDiscount) discountPrice else originalPrice
