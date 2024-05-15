package ru.livetyping.zarina.domain.order

data class OrderPrice(
    val orderPrice: Int,
    val deliveryPrice: Int,
    val totalPrice: Int,
)
