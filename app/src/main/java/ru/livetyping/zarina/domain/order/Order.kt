package ru.livetyping.zarina.domain.order

import java.time.LocalDate

sealed class Order(
    open val id: Id,
    open val number: Number,
    open val productCount: Int,
    open val date: LocalDate,
    open val status: Status,
    open val totalPrice: Long,
) {
    @JvmInline
    value class Id(val value: Long)

    @JvmInline
    value class Number(val value: String)

    enum class Status {
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
}
