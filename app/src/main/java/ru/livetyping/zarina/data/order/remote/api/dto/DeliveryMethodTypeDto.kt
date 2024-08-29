package ru.livetyping.zarina.data.order.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.order.DeliveryMethodType

@Serializable
@JvmInline
value class DeliveryMethodTypeDto(val value: String) {
    fun toDeliveryMethodType(): DeliveryMethodType = when (value) {
        "delivery_service" -> DeliveryMethodType.DELIVERY_SERVICE
        "post" -> DeliveryMethodType.POST
        "pickup" -> DeliveryMethodType.PICKUP
        "retail" -> DeliveryMethodType.RETAIL
        "yandex" -> DeliveryMethodType.YANDEX
        "express" -> DeliveryMethodType.EXPRESS
        else -> error("Unknown delivery method type $value")
    }
}
