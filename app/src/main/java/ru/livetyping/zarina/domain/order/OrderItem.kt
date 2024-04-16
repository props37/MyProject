package ru.livetyping.zarina.domain.order

import ru.livetyping.zarina.domain.common.Url
import java.time.LocalDate

data class OrderItem(
    override val id: Id,
    override val number: Number,
    override val productCount: Int,
    override val date: LocalDate,
    override val status: Status,
    override val totalPrice: Long,
    val products: List<Product>,
) : Order(
    id = id,
    number = number,
    productCount = productCount,
    date = date,
    status = status,
    totalPrice = totalPrice,
) {
    data class Product(
        val imageUrl: Url,
        val count: Int,
    )
}
