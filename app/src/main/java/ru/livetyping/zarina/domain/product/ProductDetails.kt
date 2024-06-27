package ru.livetyping.zarina.domain.product

import ru.livetyping.zarina.domain.common.Color
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
    val label: Label?,
    val description: List<DescriptionEntry>,
    val bonusCountForPurchase: Int,
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
    data class Label(
        val name: String,
        val color: Color,
    )

    data class DescriptionEntry(
        val title: String,
        val body: String,
    )
}
