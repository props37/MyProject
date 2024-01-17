package ru.zarina.zarina.domain.rework.common

data class Product(
    val id: Id,
    val name: String,
    val price: Price,
    val colors: List<ProductColor>,
    val media: List<Media>,
) {
    @JvmInline
    value class Id(val value: String)
}
