package ru.livetyping.zarina.data.wishlist.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull

@Serializable
internal data class WishlistProductIdsDto(
    @SerialName("items_count")
    val itemCount: Int? = null,

    @SerialName("items")
    val items: List<String>? = null,
) {
    fun toProductIds(): Set<Product.Id> {
        checkPropertyNotNull(items) { ::items }
        return items.mapTo(mutableSetOf()) { Product.Id(it) }
    }
}
