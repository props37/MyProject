package ru.livetyping.zarina.core.network.zarina.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType

@Serializable
@JvmInline
public value class DeliveryMethodTypeDto(public val value: String) {
    public fun toDeliveryMethodType(): DeliveryMethodType = when (value) {
        VALUE_DELIVERY_SERVICE -> DeliveryMethodType.COURIER
        VALUE_POST -> DeliveryMethodType.POST
        VALUE_PICKUP -> DeliveryMethodType.PICKUP_FROM_PICKUP_POINT
        VALUE_PICKUP_IN_STORE -> DeliveryMethodType.PICKUP_FROM_STORE_WAREHOUSE
        VALUE_RETAIL -> DeliveryMethodType.PICKUP_FROM_STORE
        VALUE_YANDEX -> DeliveryMethodType.YANDEX_EXPRESS
        VALUE_EXPRESS -> DeliveryMethodType.COURIER_EXPRESS
        else -> error("Unknown delivery method type $value")
    }

    public companion object {
        public fun from(type: DeliveryMethodType): DeliveryMethodTypeDto {
            return when (type) {
                DeliveryMethodType.COURIER -> DeliveryMethodTypeDto(VALUE_DELIVERY_SERVICE)
                DeliveryMethodType.POST -> DeliveryMethodTypeDto(VALUE_POST)
                DeliveryMethodType.PICKUP_FROM_PICKUP_POINT -> DeliveryMethodTypeDto(VALUE_PICKUP)
                DeliveryMethodType.PICKUP_FROM_STORE_WAREHOUSE -> DeliveryMethodTypeDto(VALUE_PICKUP_IN_STORE)
                DeliveryMethodType.PICKUP_FROM_STORE -> DeliveryMethodTypeDto(VALUE_RETAIL)
                DeliveryMethodType.YANDEX_EXPRESS -> DeliveryMethodTypeDto(VALUE_YANDEX)
                DeliveryMethodType.COURIER_EXPRESS -> DeliveryMethodTypeDto(VALUE_EXPRESS)
            }
        }

        private const val VALUE_DELIVERY_SERVICE = "delivery_service"
        private const val VALUE_POST = "post"
        private const val VALUE_PICKUP = "pickup"
        private const val VALUE_PICKUP_IN_STORE = "pickupinstore"
        private const val VALUE_RETAIL = "retail"
        private const val VALUE_YANDEX = "yandex"
        private const val VALUE_EXPRESS = "express"
    }
}
