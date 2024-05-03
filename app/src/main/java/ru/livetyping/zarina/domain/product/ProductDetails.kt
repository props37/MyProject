package ru.livetyping.zarina.domain.product

import ru.livetyping.zarina.domain.common.Media
import ru.livetyping.zarina.domain.common.Url

data class ProductDetails(
    override val id: Id,
    override val name: String,
    override val price: Price,
    override val offers: List<ProductOffer>,
    override val colors: List<ProductColor>,
    override val media: List<Media>,
    override val isInFavorites: Boolean,
    override val isInCart: Boolean,
    val shareUrl: Url?,
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
