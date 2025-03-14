package ru.livetyping.zarina.data.product.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Color
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.core.network.zarina.dto.MediaDto
import ru.livetyping.zarina.core.network.zarina.dto.ProductColorDto
import ru.livetyping.zarina.core.network.zarina.dto.ProductOfferDto
import ru.livetyping.zarina.core.network.zarina.dto.ProductPriceDto
import timber.log.Timber

@Serializable
internal data class ProductDetailedDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("price")
    val price: ProductPriceDto? = null,

    @SerialName("offers")
    val offers: List<ProductOfferDto>? = null,

    @SerialName("colors")
    val colors: List<ProductColorDto>? = null,

    @SerialName("media")
    val media: List<MediaDto>? = null,

    @SerialName("description")
    val description: List<DescriptionEntryDto>? = null,

    @SerialName("bonus")
    val bonus: Int? = null,

    @SerialName("label")
    val label: LabelDto? = null,

    @SerialName("threshold")
    val threshold: Int? = null,

    @SerialName("share_url")
    val shareUrl: String? = null,
) {
    fun toProductDetailed(): ProductDetailed {
        checkPropertyNotNull(id) { ::id }
        checkPropertyNotNull(name) { ::name }
        checkPropertyNotNull(price) { ::price }
        checkPropertyNotNull(offers) { ::offers }
        checkPropertyNotNull(colors) { ::colors }
        checkPropertyNotNull(media) { ::media }
        checkPropertyNotNull(description) { ::description }
        return ProductDetailed(
            id = Product.Id(id),
            name = name,
            price = price.toProductPrice(),
            offers = offers.mapNotNull { it.toProductOffer() },
            colors = colors.mapNotNull { it.toProductColor() },
            media = media.mapNotNull { it.toMedia() },
            // States that are not present in the DTO
            isInWishlist = false,
            isInCart = false,
            label = label?.toLabel(),
            description = description.mapNotNull { it.toDescriptionEntry() },
            bonusAccrualForPurchase = bonus ?: 0,
            freeDeliveryTotalPriceThreshold = threshold ?: 0,
            shareUrl = shareUrl?.let { Url.create(it) },
        )
    }

    @Serializable
    data class LabelDto(
        @SerialName("title")
        val name: String? = null,

        @SerialName("color")
        val color: String? = null,
    ) {
        fun toLabel(): ProductDetailed.Label? {
            return if (name != null && color != null) {
                ProductDetailed.Label(
                    name = name,
                    color = Color(color.trim()),
                )
            } else {
                Timber.tag(TAG).e("Drop label $this because its name or color is null")
                null
            }
        }
    }

    @Serializable
    data class DescriptionEntryDto(
        @SerialName("title")
        val title: String? = null,

        @SerialName("text")
        val body: String? = null,
    ) {
        fun toDescriptionEntry(): ProductDetailed.DescriptionEntry? {
            return if (title != null && body != null) {
                ProductDetailed.DescriptionEntry(
                    title = title,
                    body = body,
                )
            } else {
                Timber.tag(TAG).e("Drop description entry $this because its title or body is null")
                null
            }
        }
    }

    private companion object {
        private const val TAG = "ProductDetailedDto"
    }
}
