package ru.zarina.zarina.data.rework.common.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.product.Product

@Serializable
data class ProductDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("price")
    val price: PriceDto? = null,

    @SerialName("colors")
    val colors: List<ProductColorDto>? = null,

    @SerialName("media")
    val media: List<MediaDto>? = null,
) {
    fun toProduct(): Product {
        checkNotNull(id) { "id is null" }
        checkNotNull(price) { "price is null" }
        val colors = colors?.map { it.toProductColor() } ?: emptyList()
        val media = media?.map { it.toMedia() } ?: emptyList()
        return Product(
            id = Product.Id(id),
            name = checkNotNull(name) { "name is null" },
            price = price.toPrice(),
            colors = colors,
            media = media,
            isInFavorites = false, // The state is not present in this DTO
        )
    }
}
