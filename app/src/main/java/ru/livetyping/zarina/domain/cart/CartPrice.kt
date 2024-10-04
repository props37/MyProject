package ru.livetyping.zarina.domain.cart

data class CartPrice(
    val cartPrice: Int,
    val discountSize: Int,
    val totalPrice: Int,
    val deliveryPrice: Int?,
)
