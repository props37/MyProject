package ru.livetyping.zarina.core.analytics.model

import java.math.BigDecimal

public data class Product(
    val id: String,
    val name: String,
    val currentPrice: BigDecimal,
    val originalPrice: BigDecimal,
)
