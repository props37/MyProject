package ru.livetyping.zarina.core.domain.model.order

import java.math.BigDecimal
import java.time.LocalDate

// Marked as stable on config/compose/stability_config.txt
public sealed class Order {
    public abstract val id: Id
    public abstract val number: Number
    public abstract val productCount: Int
    public abstract val date: LocalDate
    public abstract val status: OrderStatus
    public abstract val totalPrice: BigDecimal

    public val isPaid: Boolean get() = status == OrderStatus.PAID

    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Id(public val value: String)

    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Number(public val value: String)
}
