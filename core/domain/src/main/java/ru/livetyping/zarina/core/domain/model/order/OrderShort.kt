package ru.livetyping.zarina.core.domain.model.order

import ru.livetyping.zarina.core.domain.model.common.Url
import java.math.BigDecimal
import java.time.LocalDate

// Marked as stable on config/compose/stability_config.txt
public data class OrderShort(
    override val id: Id,
    override val number: Number,
    override val productCount: Int,
    override val date: LocalDate,
    override val status: OrderStatus,
    override val totalPrice: BigDecimal,
    val products: List<Product>,
) : Order() {
    // Marked as stable on config/compose/stability_config.txt
    public data class Product(
        val imageUrl: Url,
        val count: Int,
    )
}
