package ru.livetyping.zarina.data.order.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.order.Order

@Serializable
@JvmInline
value class OrderStatusDto(val value: String) {
    fun toOrderStatus(): Order.Status = TODO("Not yet implemented")
}
