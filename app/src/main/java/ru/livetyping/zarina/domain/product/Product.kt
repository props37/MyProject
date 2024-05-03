package ru.livetyping.zarina.domain.product

import ru.livetyping.zarina.domain.common.Media

sealed class Product(
    open val id: Id,
    open val name: String,
    open val price: Price,
    open val offers: List<ProductOffer>,
    open val colors: List<ProductColor>,
    open val media: List<Media>,
    open val isInFavorites: Boolean,
    open val isInCart: Boolean,
) {
    val isAvailable: Boolean by lazy { offers.any { it.isAvailable } }

    @JvmInline
    value class Id(val value: String)
}
