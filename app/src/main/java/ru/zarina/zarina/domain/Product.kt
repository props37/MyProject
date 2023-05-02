package ru.zarina.zarina.domain

data class Product(
    val id: Id,
    val name: String,
    val media: List<Media>,
    val price: Price,
    val colorVariants: Map<Color, Variant>,
    val offers: List<Offer>,
    val description: List<Pair<String, String>>,
    val url: Url?,
    val attributes: List<String>,
    /** Whether this product is a part of "complete look" ("образ целиком") bundle */
    val isLookPart: Boolean,
) {
    @JvmInline
    value class Id(val value: String)

    data class Variant(
        val id: Id,
        val isCurrent: Boolean,
    )
}
