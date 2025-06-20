package ru.livetyping.zarina.core.uimodel.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.order.OrderStatus

@Serializable
@Parcelize
public enum class OrderStatusParcelable : Parcelable {
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
    NOT_REFUNDABLE,
    TO_DELIVERY;

    public fun toOrderStatus(): OrderStatus = when (this) {
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
        TO_DELIVERY -> OrderStatus.TO_DELIVERY
    }

    public companion object {
        public fun from(status: OrderStatus): OrderStatusParcelable = when (status) {
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
            OrderStatus.TO_DELIVERY -> TO_DELIVERY
        }
    }
}
