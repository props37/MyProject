package ru.livetyping.zarina.data.order.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.order.DeliveryMethodType

@Serializable
@JvmInline
value class DeliveryMethodTypeDto(val value: String) {
    fun toDeliveryMethodType(): DeliveryMethodType = when (value) {
        VALUE_DELIVERY_SERVICE -> DeliveryMethodType.DELIVERY_SERVICE
        VALUE_POST -> DeliveryMethodType.POST
        VALUE_PICKUP -> DeliveryMethodType.PICKUP
        VALUE_PICKUP_STORE -> DeliveryMethodType.PICKUP_STORE
        VALUE_RETAIL -> DeliveryMethodType.RETAIL
        VALUE_YANDEX -> DeliveryMethodType.YANDEX
        VALUE_EXPRESS -> DeliveryMethodType.EXPRESS
        else -> error("Unknown delivery method type $value")
    }

    companion object {
        fun from(type: DeliveryMethodType): DeliveryMethodTypeDto {
            return when (type) {
                DeliveryMethodType.DELIVERY_SERVICE -> DeliveryMethodTypeDto(VALUE_DELIVERY_SERVICE)
                DeliveryMethodType.POST -> DeliveryMethodTypeDto(VALUE_POST)
                DeliveryMethodType.PICKUP -> DeliveryMethodTypeDto(VALUE_PICKUP)
                DeliveryMethodType.PICKUP_STORE -> DeliveryMethodTypeDto(VALUE_PICKUP_STORE)
                DeliveryMethodType.RETAIL -> DeliveryMethodTypeDto(VALUE_RETAIL)
                DeliveryMethodType.YANDEX -> DeliveryMethodTypeDto(VALUE_YANDEX)
                DeliveryMethodType.EXPRESS -> DeliveryMethodTypeDto(VALUE_EXPRESS)
            }
        }

        private const val VALUE_DELIVERY_SERVICE = "delivery_service"
        private const val VALUE_POST = "post"
        private const val VALUE_PICKUP = "pickup"
        private const val VALUE_PICKUP_STORE = "pickupinstore"
        private const val VALUE_RETAIL = "retail"
        private const val VALUE_YANDEX = "yandex"
        private const val VALUE_EXPRESS = "express"
    }
}
