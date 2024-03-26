package ru.zarina.zarina.data.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.cart.CartProductCount

@Serializable
data class CartProductCountDto(
    @SerialName("total_count")
    val cartProductCount: Int? = null,
) {
    fun toCartProductCount(): CartProductCount {
        checkNotNull(cartProductCount) { "cartProductCount is null" }
        return CartProductCount(cartProductCount)
    }
}
