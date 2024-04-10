package ru.livetyping.zarina.data.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.cart.CartProductCount

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
