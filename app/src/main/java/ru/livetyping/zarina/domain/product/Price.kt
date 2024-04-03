package ru.livetyping.zarina.domain.product

data class Price(
    val originalPrice: Long,
    val hasDiscount: Boolean,
    val discountPrice: Long,
    val discountPercent: Int,
)

val Price.currentPrice: Long
    get() = if (hasDiscount) discountPrice else originalPrice
