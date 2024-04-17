package ru.livetyping.zarina.data.order.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.order.Order

@Serializable
@JvmInline
value class OrderStatusDto(val value: String) {
    fun toOrderStatus(): Order.Status = when (value) {
        "opened" -> Order.Status.OPENED
        "approved" -> Order.Status.APPROVED
        "paid" -> Order.Status.PAID
        "in_transit" -> Order.Status.IN_TRANSIT
        "delivered" -> Order.Status.DELIVERED
        "ready_for_pickup" -> Order.Status.READY_FOR_PICKUP
        "cancelled" -> Order.Status.CANCELLED
        "refunding" -> Order.Status.REFUNDING
        "approved_to_refund" -> Order.Status.APPROVED_TO_REFUND
        "refunded" -> Order.Status.REFUNDED
        "non_refundable" -> Order.Status.NOT_REFUNDABLE
        else -> error("Unknown order status $value")
    }
}
