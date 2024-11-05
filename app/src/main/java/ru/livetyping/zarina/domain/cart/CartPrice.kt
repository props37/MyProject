package ru.livetyping.zarina.domain.cart

data class CartPrice(
    val cartPrice: Int,
    val discountSize: Int,
    val totalPrice: Int, // TODO: [High] Rename to finalPrice
    val deliveryPrice: Int?,
)
