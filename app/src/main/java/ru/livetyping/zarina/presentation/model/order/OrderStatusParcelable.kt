package ru.livetyping.zarina.presentation.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.order.OrderStatus

@Serializable
@Parcelize
enum class OrderStatusParcelable : Parcelable {
    OPENED,
    APPROVED,
    PAID,
    IN_TRANSIT,
    DELIVERED,
    READY_FOR_PICKUP,
    CANCELLED,
    REFUNDING,
    APPROVED_TO_REFUND,
    REFUNDED,
    NOT_REFUNDABLE;

    fun toOrderStatus(): OrderStatus = when (this) {
        OPENED -> OrderStatus.OPENED
        APPROVED -> OrderStatus.APPROVED
        PAID -> OrderStatus.PAID
        IN_TRANSIT -> OrderStatus.IN_TRANSIT
        DELIVERED -> OrderStatus.DELIVERED
        READY_FOR_PICKUP -> OrderStatus.READY_FOR_PICKUP
        CANCELLED -> OrderStatus.CANCELLED
        REFUNDING -> OrderStatus.REFUNDING
        APPROVED_TO_REFUND -> OrderStatus.APPROVED_TO_REFUND
        REFUNDED -> OrderStatus.REFUNDED
        NOT_REFUNDABLE -> OrderStatus.NOT_REFUNDABLE
    }

    companion object {
        fun from(status: OrderStatus): OrderStatusParcelable = when (status) {
            OrderStatus.OPENED -> OPENED
            OrderStatus.APPROVED -> APPROVED
            OrderStatus.PAID -> PAID
            OrderStatus.IN_TRANSIT -> IN_TRANSIT
            OrderStatus.DELIVERED -> DELIVERED
            OrderStatus.READY_FOR_PICKUP -> READY_FOR_PICKUP
            OrderStatus.CANCELLED -> CANCELLED
            OrderStatus.REFUNDING -> REFUNDING
            OrderStatus.APPROVED_TO_REFUND -> APPROVED_TO_REFUND
            OrderStatus.REFUNDED -> REFUNDED
            OrderStatus.NOT_REFUNDABLE -> NOT_REFUNDABLE
        }
    }
}
