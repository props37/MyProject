package ru.livetyping.zarina.data.cart.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.cart.CartType

@Serializable
@JvmInline
value class CartTypeDto(val value: String) {
    companion object {
        fun from(cartType: CartType): CartTypeDto = when (cartType) {
            CartType.DELIVERY -> CartTypeDto(VALUE_DELIVERY)
            CartType.PICKUP -> CartTypeDto(VALUE_PICKUP)
        }

        private const val VALUE_DELIVERY = "delivery"
        private const val VALUE_PICKUP = "retail"
    }
}
