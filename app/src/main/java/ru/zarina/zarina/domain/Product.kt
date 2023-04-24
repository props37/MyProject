package ru.zarina.zarina.domain

data class Product(
    val id: String,
    val media: List<Media>,
    val price: Price,
)
