package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.core.network.zarina.dto.CartDto

@Serializable
internal data class CheckoutCartDto(
    @SerialName("basket")
    val basket: CartDto? = null,
) {
    fun toCart(cartType: CartType): Cart {
        checkPropertyNotNull(basket) { ::basket }
        return basket.toCart(cartType)
    }
}
