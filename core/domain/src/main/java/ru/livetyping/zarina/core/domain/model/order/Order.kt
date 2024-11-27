package ru.livetyping.zarina.core.domain.model.order

import java.time.LocalDate

public sealed class Order(
    public open val id: Id,
    public open val number: Number,
    public open val productCount: Int,
    public open val date: LocalDate,
    public open val status: OrderStatus,
    public open val totalPrice: Int,
) {
    public val isPaid: Boolean get() = status == OrderStatus.PAID

    @JvmInline
    public value class Id(public val value: String)

    @JvmInline
    public value class Number(public val value: String)
}
