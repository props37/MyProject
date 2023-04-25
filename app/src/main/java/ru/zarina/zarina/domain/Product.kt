package ru.zarina.zarina.domain

data class Product(
    val id: String,
    val name: String,
    val media: List<Media>,
    val price: Price,
    val colorVariants: Map<Color, Variant>,
    val description: List<Pair<String, String>>,
    val url: Url?,
    val attributes: List<String>,
    /** Whether this product is a part of "complete look" ("образ целиком") bundle */
    val isLookPart: Boolean,
) {
    data class Variant(
        val id: String,
        val isCurrent: Boolean,
    )
}
