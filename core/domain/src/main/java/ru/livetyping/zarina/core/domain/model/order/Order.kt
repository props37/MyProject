package ru.livetyping.zarina.core.domain.model.order

import java.time.LocalDate

// Marked as stable on config/compose/stability_config.txt
public sealed class Order(
    public open val id: Id,
    public open val number: Number,
    public open val productCount: Int,
    public open val date: LocalDate,
    public open val status: OrderStatus,
    public open val totalPrice: Int,
) {
    public val isPaid: Boolean get() = status == OrderStatus.PAID

    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Id(public val value: String)

    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Number(public val value: String)
}
