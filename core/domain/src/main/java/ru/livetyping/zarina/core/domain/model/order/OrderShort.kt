package ru.livetyping.zarina.core.domain.model.order

import ru.livetyping.zarina.core.domain.model.common.Url
import java.time.LocalDate

public data class OrderShort(
    override val id: Id,
    override val number: Number,
    override val productCount: Int,
    override val date: LocalDate,
    override val status: OrderStatus,
    override val totalPrice: Int,
    val products: List<Product>,
) : Order(
    id = id,
    number = number,
    productCount = productCount,
    date = date,
    status = status,
    totalPrice = totalPrice,
) {
    public data class Product(
        val imageUrl: Url,
        val count: Int,
    )
}
