package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Product

@Serializable
data class ProductDto(
    val id: String?,
    val media: List<MediaDto>?,
) {
    fun toDomain(): Product? {
        return if (id == null)
            null
        else
            Product(
                id = id,
                media = media?.mapNotNull { it.toDomain() }.orEmpty()
            )
    }
}
