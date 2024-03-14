package ru.zarina.zarina.data.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.cart.Cart

@Serializable
data class CartDto(
    @SerialName("items")
    val products: List<CartProductDto>? = null,
) {
    fun toCart(): Cart {
        checkNotNull(products) { "products is null" }
        return Cart(
            products = products.map { it.toCartProduct() },
        )
    }
}
