package ru.zarina.zarina.data.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.cart.CartProductIds
import ru.zarina.zarina.domain.product.Product

@Serializable
data class CartProductIdsDto(
    @SerialName("items_count")
    val itemCount: Int? = null,

    @SerialName("items")
    val items: List<String>? = null,
) {
    fun toCartProductIds(): CartProductIds {
        checkNotNull(items) { "items is null" }
        return CartProductIds(
            cartProductIds = items.map { Product.Id(it) }.toSet(),
            cartProductCount = checkNotNull(itemCount) { "itemCount is null" },
        )
    }
}
