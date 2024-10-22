package ru.livetyping.zarina.core.domain.model.product

import ru.livetyping.zarina.core.domain.model.common.Color
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.media.Media

public data class ProductDetailed(
    override val id: Id,
    override val name: String,
    override val price: ProductPrice,
    override val offers: List<ProductOffer>,
    override val colors: List<ProductColor>,
    override val media: List<Media>,
    override val isInFavorites: Boolean,
    override val isInCart: Boolean,
    val label: Label?,
    val description: List<DescriptionEntry>,
    val bonusAccrualForPurchase: Int,
    val freeDeliveryTotalPriceThreshold: Int,
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
) {
    public data class Label(
        val name: String,
        val color: Color,
    )

    public data class DescriptionEntry(
        val title: String,
        val body: String,
    )
}
