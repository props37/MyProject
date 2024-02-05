package ru.zarina.zarina.data.rework.common.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.common.MediaType
import ru.zarina.zarina.domain.rework.product.Product

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
    fun toProduct(): Product {
        checkNotNull(id) { "id is null" }
        checkNotNull(price) { "price is null" }
        val offers = offers?.mapNotNull { it.toProductOffer() } ?: emptyList()
        val colors = colors?.mapNotNull { it.toProductColor() } ?: emptyList()
        val media = media
            ?.mapNotNull { it.toMedia() }
            // Filter out videos until a good decision is found on how to display multiple videos
            // simultaneously in product list
            ?.filter { it.type == MediaType.IMAGE }
            ?: emptyList()
        return Product(
            id = Product.Id(id),
            name = checkNotNull(name) { "name is null" },
            price = price.toPrice(),
            offers = offers,
            colors = colors,
            media = media,
            // States that are not present in the DTO
            isInFavorites = false,
            isInCart = false,
        )
    }
}
