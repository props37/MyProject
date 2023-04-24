package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Url
import ru.zarina.zarina.utils.kotlin.isNotNull

@Serializable
data class ProductDto(
    @SerialName("id")
    val id: String?,
    @SerialName("media")
    val media: List<MediaDto>?,
    @SerialName("price")
    val price: PriceDto,
    @SerialName("colors")
    val colors: List<ColorDto>,
    @SerialName("description")
    val description: List<DescriptionItemDto>,
    @SerialName("share_url")
    val url: String?,
) {
    fun toDomain(): Product? {
        val price = price.toDomain()
        if (
            isNotNull(id, "id")
            && isNotNull(price, "price")
        ) return Product(
            id = id,
            media = media?.mapNotNull { it.toDomain() }.orEmpty(),
            price = price,
            colorVariants = colors
                .mapNotNull { it.toDomain() }
                .associate { (color, variant) -> color to variant },
            description = description.mapNotNull { it.toDomain() },
            url = url?.let { Url(it) },
        )
        return null
    }
}
