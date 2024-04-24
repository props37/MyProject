package ru.livetyping.zarina.data.order.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.order.OrderDeliveryMethod

@Serializable
@JvmInline
value class OrderDeliveryMethodDto(val value: String) {
    fun toOrderDeliveryMethod(): OrderDeliveryMethod = when (value) {
        "delivery_service" -> OrderDeliveryMethod.DELIVERY_SERVICE
        "post" -> OrderDeliveryMethod.POST
        "pickup" -> OrderDeliveryMethod.PICK_UP
        "retail" -> OrderDeliveryMethod.RETAIL
        "yandex" -> OrderDeliveryMethod.YANDEX
        else -> error("Unknown delivery method $value")
    }
}
