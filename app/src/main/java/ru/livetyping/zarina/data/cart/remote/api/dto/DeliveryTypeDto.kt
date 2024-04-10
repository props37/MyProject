package ru.livetyping.zarina.data.cart.remote.api.dto

import ru.livetyping.zarina.domain.cart.DeliveryType

@JvmInline
value class DeliveryTypeDto(val value: String) {
    companion object {
        fun fromDeliveryType(deliveryType: DeliveryType): DeliveryTypeDto = when (deliveryType) {
            DeliveryType.DELIVERY -> DeliveryTypeDto(VALUE_DELIVERY)
            DeliveryType.PICK_UP_FROM_SHOP -> DeliveryTypeDto(VALUE_PICK_UP_FROM_SHOP)
        }

        private const val VALUE_DELIVERY = "delivery"
        private const val VALUE_PICK_UP_FROM_SHOP = "retail"
    }
}
