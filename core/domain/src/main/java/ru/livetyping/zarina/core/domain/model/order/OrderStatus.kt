package ru.livetyping.zarina.core.domain.model.order

public enum class OrderStatus {
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
}
