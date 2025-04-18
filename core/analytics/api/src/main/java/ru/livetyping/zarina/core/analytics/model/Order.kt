package ru.livetyping.zarina.core.analytics.model

public data class Order(
    val id: String,
    val products: List<CartProduct>,
)
