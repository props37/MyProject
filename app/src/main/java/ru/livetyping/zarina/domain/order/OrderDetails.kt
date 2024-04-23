package ru.livetyping.zarina.domain.order

import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.product.Price
import ru.livetyping.zarina.domain.product.ProductColor
import java.time.LocalDate
import ru.livetyping.zarina.domain.product.Product as DomainProduct

data class OrderDetails(
    override val id: Id,
    override val number: Number,
    override val productCount: Int,
    override val date: LocalDate,
    override val status: OrderStatus,
    val price: OrderPrice,
    val products: List<Product>,
) : Order(
    id = id,
    number = number,
    productCount = productCount,
    date = date,
    status = status,
    totalPrice = price.totalPrice,
) {
    data class Product(
        val id: DomainProduct.Id,
        val name: String,
        val size: String,
        val color: ProductColor,
        val imageUrl: Url,
        val price: Price,
        val count: Int,
    )
}
