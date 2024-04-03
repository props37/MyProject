package ru.livetyping.zarina.domain.old

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import timber.log.Timber

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
    val isFavorite: Boolean,
    private val _isAvailable: Boolean? = null,
) {

    val isAvailable by lazy { _isAvailable ?: offers.any { it.isAvailable } }
    val color by lazy {
        colorVariants
            .entries
            .firstOrNull { (_, variant) -> variant.isCurrent }
            .also { if (it == null) Timber.w("Product doesn't have a current color") }
            ?.key
    }

    @JvmInline
    value class Id(val value: String)

    data class Variant(
        val id: Id,
        val isCurrent: Boolean,
    )
}
