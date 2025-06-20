package ru.livetyping.zarina.core.network.zarina.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.order.OrderStatus
import timber.log.Timber

@Serializable
@JvmInline
public value class OrderStatusDto(public val value: String) {
    public fun toOrderStatus(): OrderStatus = when (value) {
        "opened" -> OrderStatus.OPENED
        "approved" -> OrderStatus.APPROVED
        "paid" -> OrderStatus.PAID
        "in_transit" -> OrderStatus.IN_TRANSIT
        "delivered" -> OrderStatus.DELIVERED
        "ready_for_pickup" -> OrderStatus.READY_FOR_PICKUP
        "cancelled" -> OrderStatus.CANCELLED
        "refunding" -> OrderStatus.REFUNDING
        "approved_to_refund" -> OrderStatus.APPROVED_TO_REFUND
        "refunded" -> OrderStatus.REFUNDED
        "non_refundable" -> OrderStatus.NOT_REFUNDABLE
        "to_delivery" -> OrderStatus.TO_DELIVERY
        else -> {
            Timber.tag(TAG).w("Unknown order status $this")
            OrderStatus.UNKNOWN
        }
    }

    private companion object {
        private const val TAG = "OrderStatusDto"
    }
}
