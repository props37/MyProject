package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Url

@Serializable
data class ProductDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("media")
    val media: List<MediaDto>? = null,
    @SerialName("price")
    val price: PriceDto? = null,
    @SerialName("colors")
    val colors: List<ColorDto>? = null,
    @SerialName("description")
    val description: List<DescriptionItemDto>? = null,
    @SerialName("has_total_look")
    val isLookPart: Boolean? = null,
    @SerialName("share_url")
    val url: String? = null,
    @SerialName("attributes")
    val attributes: List<String>? = null,
    @SerialName("sizes")
    val sizes: List<SizeDto>? = null,
) {
    fun toDomain(): Product? {
        val price = price?.toDomain()
        if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(price, "price")
            && ApiContract.isNotNull(name, "name")
        ) return Product(
            id = Product.Id(id),
            name = name,
            media = media
                ?.mapNotNull { it.toDomain() }
                .orEmpty(),
            price = price,
            colorVariants = colors
                ?.mapNotNull { it.toDomain() }
                ?.associate { (color, variant) -> color to variant }
                .orEmpty(),
            offers = sizes
                ?.mapNotNull { it.toDomain() }
                .orEmpty(),
            description = description
                ?.mapNotNull { it.toDomain() }
                .orEmpty(),
            url = url?.let { Url(it) },
            isLookPart = isLookPart == true,
            attributes = attributes.orEmpty(),
        )
        return null
    }
}
