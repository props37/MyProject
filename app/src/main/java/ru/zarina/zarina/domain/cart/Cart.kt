package ru.zarina.zarina.domain.cart

data class Cart(
    val products: List<CartProduct>,
    val size: CartSize,
)
