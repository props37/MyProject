package ru.zarina.zarina.data.search.remote.api.dto

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.domain.Price
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Url

@Serializable
data class ProductDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("available")
    val isAvailable: Boolean? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("price")
    val price: String? = null,
    @SerialName("oldPrice")
    val oldPrice: String? = null,
    @SerialName("attributes")
    val attributes: AttributesDto? = null,
    @SerialName("link_url")
    val linkUrl: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("isFavorite")
    val isFavorite: Boolean? = null,
) {
    fun toDomain(): Product? {
        val priceInt = price?.toIntOrNull()
        return if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(name, "name")
            && ApiContract.isNotNull(priceInt, "price")
        ) {
            val image = imageUrl?.let { url ->
                Media(url = Url(url), type = Media.Type.IMAGE)
            }
            Product(
                id = Product.Id(id),
                name = name,
                media = listOfNotNull(image).toImmutableList(),
                price = Price(current = priceInt, original = oldPrice?.toIntOrNull() ?: priceInt),
                colorVariants = persistentMapOf(),
                offers = persistentListOf(),
                description = persistentListOf(),
                url = linkUrl?.let { Url(it) },
                // These attributes are not the same as DTO attributes
                attributes = persistentListOf(),
                isLookPart = false,
                isFavorite = isFavorite ?: false,
            )
        } else {
            null
        }
    }
}
