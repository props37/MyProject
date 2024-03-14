package ru.zarina.zarina.data.favorite.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.product.Product

@Serializable
data class FavoriteProductIdsDto(
    @SerialName("items_count") 
    val itemCount: Int? = null,
    
    @SerialName("items") 
    val items: List<String>? = null,
) {
    fun toFavoriteProductIds(): Set<Product.Id> {
        checkNotNull(items) { "items is null" }
        return items.map { Product.Id(it) }.toSet()
    }
}
