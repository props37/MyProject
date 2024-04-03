package ru.livetyping.zarina.data.common.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.MediaType
import ru.livetyping.zarina.domain.product.Product
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
        val media = media
            ?.mapNotNull { it.toMedia() }
            // Filter out videos until a good decision is found on how to display multiple videos
            // simultaneously in product list
            ?.filter { it.type == MediaType.IMAGE }
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
