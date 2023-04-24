package ru.zarina.zarina.domain

data class Product(
    val id: String,
    val media: List<Media>,
    val price: Price,
    val colorVariants: Map<Color, Variant>,
) {
    data class Variant(
        val id: String,
        val isCurrent: Boolean,
    )
}
