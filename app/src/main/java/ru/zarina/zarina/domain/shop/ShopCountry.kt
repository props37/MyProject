package ru.zarina.zarina.domain.shop

data class ShopCountry(
    val id: String,
    val name: String,
    val cities: List<ShopCity>,
)
