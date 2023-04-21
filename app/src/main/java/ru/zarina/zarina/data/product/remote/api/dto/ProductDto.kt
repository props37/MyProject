package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.utils.kotlin.isNotNull

@Serializable
data class ProductDto(
    val id: String?,
    val media: List<MediaDto>?,
) {
    fun toDomain(): Product? {
        if (isNotNull(id, "id")) return Product(
            id = id,
            media = media?.mapNotNull { it.toDomain() }.orEmpty()
        )
        return null
    }
}
