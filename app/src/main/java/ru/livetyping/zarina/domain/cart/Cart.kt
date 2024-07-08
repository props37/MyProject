package ru.livetyping.zarina.domain.cart

data class Cart(
    val products: List<CartProduct>,
    val size: CartSize,
    val price: CartPrice,
)
