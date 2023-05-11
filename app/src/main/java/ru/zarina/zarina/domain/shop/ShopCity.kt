package ru.zarina.zarina.domain.shop

data class ShopCity(
    val id: String,
    val name: String,
    val shops: List<Shop>,
)
