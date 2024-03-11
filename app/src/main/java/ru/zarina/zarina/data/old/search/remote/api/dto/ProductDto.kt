package ru.zarina.zarina.data.old.search.remote.api.dto

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.old.ApiContract
import ru.zarina.zarina.domain.old.Media
import ru.zarina.zarina.domain.old.Price
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.domain.old.Url
import kotlin.math.roundToInt

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
        val priceInt = price?.toDoubleOrNull()?.roundToInt()
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
                _isAvailable = isAvailable,
                name = name.replace("Zarina", "").trim(),
                media = listOfNotNull(image).toImmutableList(),
                price = Price(
                    current = priceInt,
                    original = oldPrice?.toDoubleOrNull()?.roundToInt() ?: priceInt
                ),
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
