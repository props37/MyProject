package ru.zarina.zarina.domain.rework.product

data class Price(
    val commonPrice: Long,
    val hasDiscount: Boolean,
    val discountPrice: Long,
    val discountPercent: Int,
)
