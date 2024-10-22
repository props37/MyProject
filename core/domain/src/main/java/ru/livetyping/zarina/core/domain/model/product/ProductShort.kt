package ru.livetyping.zarina.core.domain.model.product

import ru.livetyping.zarina.core.domain.model.media.Media

public data class ProductShort(
    override val id: Id,
    override val name: String,
    override val price: ProductPrice,
    override val offers: List<ProductOffer>,
    override val colors: List<ProductColor>,
    override val media: List<Media>,
    override val isInFavorites: Boolean,
    override val isInCart: Boolean,
) : Product(
    id = id,
    name = name,
    price = price,
    offers = offers,
    colors = colors,
    media = media,
    isInFavorites = isInFavorites,
    isInCart = isInCart,
)
