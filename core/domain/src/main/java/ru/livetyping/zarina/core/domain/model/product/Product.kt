package ru.livetyping.zarina.core.domain.model.product

import ru.livetyping.zarina.core.domain.model.media.Media

public sealed class Product(
    public open val id: Id,
    public open val name: String,
    public open val price: ProductPrice,
    public open val offers: List<ProductOffer>,
    public open val colors: List<ProductColor>,
    public open val media: List<Media>,
    public open val isInWishlist: Boolean,
    public open val isInCart: Boolean,
) {
    public val isAvailable: Boolean by lazy {
        offers.any { it.isAvailable }
    }

    @JvmInline
    public value class Id(public val value: String)
}
