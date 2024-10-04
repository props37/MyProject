package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.cart.remote.api.dto.CartDto
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.cart.CartType

@Serializable
data class CheckoutCartDto(
    @SerialName("basket")
    val cart: CartDto? = null,
) {
    fun toCart(cartType: CartType): Cart {
        checkNotNull(cart) { "cart is null" }
        return cart.toCart(cartType)
    }
}
