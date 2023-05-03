package ru.zarina.zarina.domain

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

data class Product(
    val id: Id,
    val name: String,
    val media: ImmutableList<Media>,
    val price: Price,
    val colorVariants: ImmutableMap<Color, Variant>,
    val offers: ImmutableList<Offer>,
    val description: ImmutableList<Pair<String, String>>,
    val url: Url?,
    val attributes: ImmutableList<String>,
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
