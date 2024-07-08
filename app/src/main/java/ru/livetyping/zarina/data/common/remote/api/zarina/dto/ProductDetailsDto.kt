package ru.livetyping.zarina.data.common.remote.api.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.Color
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductDetails
import timber.log.Timber

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

    @SerialName("description")
    val description: List<DescriptionEntry>? = null,

    @SerialName("bonus")
    val bonusAccrualForPurchase: Int? = null,

    @SerialName("label")
    val label: Label? = null,

    @SerialName("threshold")
    val freeDeliveryTotalPriceThreshold: Int? = null,

    @SerialName("share_url")
    val shareUrl: String? = null,
) {
    fun toProductDetails(): ProductDetails {
        checkNotNull(id) { "id is null" }
        checkNotNull(name) { "name is null" }
        checkNotNull(price) { "price is null" }
        checkNotNull(offers) { "offers is null" }
        checkNotNull(colors) { "colors is null" }
        checkNotNull(media) { "media is null" }
        checkNotNull(description) { "description is null" }
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
            label = label?.toLabel(),
            description = description.mapNotNull { it.toDescriptionEntry() },
            bonusAccrualForPurchase = bonusAccrualForPurchase ?: 0,
            freeDeliveryTotalPriceThreshold = freeDeliveryTotalPriceThreshold ?: 0,
            shareUrl = shareUrl?.let { Url(it) },
        )
    }

    @Serializable
    data class Label(
        @SerialName("title")
        val name: String? = null,

        @SerialName("color")
        val color: String? = null,
    ) {
        fun toLabel(): ProductDetails.Label? {
            return if (name != null && color != null) {
                ProductDetails.Label(
                    name = name,
                    color = Color(color.trim()),
                )
            } else {
                Timber.e("Drop label because its name or color is null")
                null
            }
        }
    }

    @Serializable
    data class DescriptionEntry(
        @SerialName("title")
        val title: String? = null,

        @SerialName("text")
        val body: String? = null,
    ) {
        fun toDescriptionEntry(): ProductDetails.DescriptionEntry? {
            return if (title != null && body != null) {
                ProductDetails.DescriptionEntry(
                    title = title,
                    body = body,
                )
            } else {
                Timber.e("Drop description entry because its title or body is null")
                null
            }
        }
    }
}
