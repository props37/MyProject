package ru.livetyping.zarina.core.domain.model.order

// Marked as stable on config/compose/stability_config.txt
public data class OrderPrice(
    val orderPrice: Int,
    val deliveryPrice: Int,
    val totalPrice: Int,
)
