package ru.livetyping.zarina.data.common.remote.api.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductDetails

@Serializable
data class ProductDetailsDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("price")
    val price: PriceDto? = null,

    @SerialName("offers")
    val offers: List<ProductOfferDto>? = null,

    @SerialName("colors")
    val colors: List<ProductColorDto>? = null,

    @SerialName("media")
    val media: List<MediaDto>? = null,

    // TODO: [High] Add description
    // TODO: [High] Add measurements?
    // TODO: [High] Add gender?

    @SerialName("share_url")
    val shareUrl: String? = null,
) {
    fun toProductDetails(colorVariants: List<ProductDetails>?): ProductDetails {
        checkNotNull(id) { "id is null" }
        checkNotNull(name) { "name is null" }
        checkNotNull(price) { "price is null" }
        checkNotNull(offers) { "offers is null" }
        checkNotNull(colors) { "colors is null" }
        checkNotNull(media) { "media is null" }
        return ProductDetails(
            id = Product.Id(id),
            name = name,
            price = price.toPrice(),
            offers = offers.mapNotNull { it.toProductOffer() },
            colors = colors.mapNotNull { it.toProductColor() },
            media = media.mapNotNull { it.toMedia() },
            // States that are not present in the DTO
            isInFavorites = false,
            isInCart = false,
            colorVariants = colorVariants,
            shareUrl = shareUrl?.let { Url(it) },
        )
    }
}
