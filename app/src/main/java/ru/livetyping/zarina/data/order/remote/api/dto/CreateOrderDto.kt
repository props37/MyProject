package ru.livetyping.zarina.data.order.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.order.OrderDetails

@Serializable
data class CreateOrderDto(
    @SerialName("order")
    val order: OrderDto? = null,
) {
    fun toOrderDetails(): OrderDetails {
        checkNotNull(order) { "order is null" }
        return order.toOrderDetails(requireAddress = false)
    }
}
