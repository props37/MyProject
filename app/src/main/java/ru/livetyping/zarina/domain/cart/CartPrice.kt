package ru.livetyping.zarina.domain.cart

data class CartPrice(
    val cartPrice: Int,
    val discountSize: Int,
    val finalPrice: Int,
    val deliveryPrice: Int?,
)
