package ru.livetyping.zarina.data.cart.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.data.cart.impl.model.CartProductIds

@Serializable
internal data class CartProductIdsDto(
    @SerialName("items_count")
    val itemCount: Int? = null,

    @SerialName("items")
    val items: List<String>? = null,
) {
    fun toCartProductIds(): CartProductIds {
        checkPropertyNotNull(items) { ::items }
        return CartProductIds(
            cartProductIds = items.mapTo(mutableSetOf()) { Product.Id(it) },
            cartProductCount = checkPropertyNotNull(itemCount) { ::itemCount },
        )
    }
}
