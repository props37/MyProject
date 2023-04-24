package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.utils.kotlin.isNotNull

@Serializable
data class ProductDto(
    @SerialName("id")
    val id: String?,
    @SerialName("media")
    val media: List<MediaDto>?,
    @SerialName("price")
    val price: PriceDto,
) {
    fun toDomain(): Product? {
        val price = price.toDomain()
        if (
            isNotNull(id, "id")
            && isNotNull(price, "price")
        ) return Product(
            id = id,
            media = media?.mapNotNull { it.toDomain() }.orEmpty()
        )
        return null
    }
}
