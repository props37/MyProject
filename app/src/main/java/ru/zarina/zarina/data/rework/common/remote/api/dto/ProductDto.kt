package ru.zarina.zarina.data.rework.common.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.product.Product
import timber.log.Timber

@Serializable
data class ProductDto(
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
) {
    fun toProduct(): Product? {
        val offers = offers?.mapNotNull { it.toProductOffer() }
        val colors = colors?.mapNotNull { it.toProductColor() }
        val media = media?.mapNotNull { it.toMedia() }
        return if (
            id != null
            && name != null
            && price != null
            && !offers.isNullOrEmpty()
            && !colors.isNullOrEmpty()
            && !media.isNullOrEmpty()
        ) {
            Product(
                id = Product.Id(id),
                name = name,
                price = price.toPrice(),
                offers = offers,
                colors = colors,
                media = media,
                // States that are not present in the DTO
                isInFavorites = false,
                isInCart = false,
            )
        } else {
            Timber.e("Drop Product because its ID, name, price, offers, colors or media is null")
            null
        }
    }
}
