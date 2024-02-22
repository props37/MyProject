package ru.zarina.zarina.data.rework.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.cart.ProductAdditionToCartResult

@Serializable
data class CartProductCountDto(
    @SerialName("total_count")
    val cartProductCount: Int? = null,
) {
    fun toProductAdditionToCartResult(): ProductAdditionToCartResult {
        return ProductAdditionToCartResult(
            cartProductCount = checkNotNull(cartProductCount) { "cartProductCount is null" },
        )
    }
}
