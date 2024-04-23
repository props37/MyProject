package ru.livetyping.zarina.domain.order

data class OrderPrice(
    val orderPrice: Long,
    val deliveryPrice: Long,
    val totalPrice: Long,
)
