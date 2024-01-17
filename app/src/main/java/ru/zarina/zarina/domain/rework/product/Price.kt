package ru.zarina.zarina.domain.rework.product

import android.icu.math.BigDecimal

data class Price(
    val commonPrice: BigDecimal,
    val hasDiscount: Boolean,
    val discountPrice: BigDecimal,
    val discountPercent: Int,
)
