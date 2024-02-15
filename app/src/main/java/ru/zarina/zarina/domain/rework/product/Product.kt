package ru.zarina.zarina.domain.rework.product

import ru.zarina.zarina.domain.rework.common.Media

data class Product(
    val id: Id,
    val name: String,
    val price: Price,
    val offers: List<ProductOffer>,
    val colors: List<ProductColor>,
    val media: List<Media>,
    val isInFavorites: Boolean,
    val isInCart: Boolean,
) {
    val isAvailable: Boolean by lazy { offers.any { it.isAvailable } }

    @JvmInline
    value class Id(val value: String)
}
