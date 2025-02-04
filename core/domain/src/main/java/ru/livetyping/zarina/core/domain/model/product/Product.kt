package ru.livetyping.zarina.core.domain.model.product

import ru.livetyping.zarina.core.domain.model.media.Media

// Marked as stable on config/compose/stability_config.txt
public sealed class Product {
    public abstract val id: Id
    public abstract val name: String
    public abstract val price: ProductPrice
    public abstract val offers: List<ProductOffer>
    public abstract val colors: List<ProductColor>
    public abstract val media: List<Media>
    public abstract val isInWishlist: Boolean
    public abstract val isInCart: Boolean

    public val isAvailable: Boolean by lazy {
        offers.any { it.isAvailable }
    }

    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Id(public val value: String)
}
