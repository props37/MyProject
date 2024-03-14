package ru.zarina.zarina.data.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.cart.Cart

@Serializable
data class CartDto(
    @SerialName("items")
    val items: List<CartProductDto>? = null,
) {
    fun toCart(): Cart = Cart()
}
