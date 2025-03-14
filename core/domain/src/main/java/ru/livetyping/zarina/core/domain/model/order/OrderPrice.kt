package ru.livetyping.zarina.core.domain.model.order

import java.math.BigDecimal

// Marked as stable on config/compose/stability_config.txt
public data class OrderPrice(
    val orderPrice: BigDecimal,
    val deliveryPrice: BigDecimal,
    val totalPrice: BigDecimal,
)
