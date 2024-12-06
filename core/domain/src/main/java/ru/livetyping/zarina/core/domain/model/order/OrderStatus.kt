package ru.livetyping.zarina.core.domain.model.order

// Marked as stable on config/compose/stability_config.txt
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
