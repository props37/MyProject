package ru.livetyping.zarina.core.network.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.media.MediaType
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import timber.log.Timber

@Serializable
public data class ProductShortDto(
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
) {
    public fun toProductShort(): ProductShort? {
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
            ProductShort(
                id = Product.Id(id),
                name = name,
                price = price.toProductPrice(),
                offers = offers,
                colors = colors,
                media = media,
                // States are not present in the DTO
                isInFavorites = false,
                isInCart = false,
            )
        } else {
            Timber.tag(TAG).e("Drop ProductShortDto because its id, name, price, offers, colors or media is null")
            null
        }
    }

    private companion object {
        private const val TAG = "ProductShortDto"
    }
}
