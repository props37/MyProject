package ru.livetyping.zarina.core.network.zarina.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.cart.CartType

@Serializable
@JvmInline
public value class CartTypeDto(public val value: String) {
    public companion object {
        public fun from(cartType: CartType): CartTypeDto = when (cartType) {
            CartType.DELIVERY -> CartTypeDto(VALUE_DELIVERY)
            CartType.PICKUP -> CartTypeDto(VALUE_PICKUP)
        }

        private const val VALUE_DELIVERY = "delivery"
        private const val VALUE_PICKUP = "retail"
    }
}
