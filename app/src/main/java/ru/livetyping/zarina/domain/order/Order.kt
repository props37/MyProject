package ru.livetyping.zarina.domain.order

import java.time.LocalDate

sealed class Order(
    open val id: Id,
    open val number: Number,
    open val productCount: Int,
    open val date: LocalDate,
    open val status: OrderStatus,
    open val totalPrice: Int,
) {
    @JvmInline
    value class Id(val value: Long)

    @JvmInline
    value class Number(val value: String)
}
