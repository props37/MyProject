package ru.livetyping.zarina.data.order.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.PaginationInfoDto
import ru.livetyping.zarina.domain.common.Page
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.order.OrderItem
import java.time.LocalDate
import ru.livetyping.zarina.domain.order.Order as DomainOrder

@Serializable
data class GetOrdersDto(
    @SerialName("items")
    val orders: List<Order>? = null,

    @SerialName("pagination")
    val paginationInfo: PaginationInfoDto? = null,
) {
    fun toOrderPage(): Page<List<OrderItem>> {
        checkNotNull(orders) { "orders is null" }
        checkNotNull(paginationInfo) { "paginationInfo is null" }
        return Page(
            data = orders.map { it.toOrderItem() },
            paginationInfo = paginationInfo.toPaginationInfo(Int.MAX_VALUE),
        )
    }

    @Serializable
    data class Order(
        @SerialName("id")
        val id: Long? = null,

        @SerialName("number")
        val number: String? = null,

        @SerialName("products_count")
        val productCount: Int? = null,

        @SerialName("date")
        val date: String? = null,

        @SerialName("status")
        val status: OrderStatusDto? = null,

        @SerialName("total_sum")
        val totalPrice: Int? = null,

        @SerialName("products")
        val products: List<Product>? = null,
    ) {
        fun toOrderItem(): OrderItem {
            checkNotNull(id) { "id is null" }
            checkNotNull(number) { "number is null" }
            checkNotNull(productCount) { "productCount is null" }
            checkNotNull(date) { "date is null" }
            checkNotNull(status) { "status is null" }
            checkNotNull(totalPrice) { "totalPrice is null" }
            checkNotNull(products) { "products is null" }
            return OrderItem(
                id = DomainOrder.Id(id),
                number = DomainOrder.Number(number),
                productCount = productCount,
                date = LocalDate.parse(date),
                status = status.toOrderStatus(),
                totalPrice = totalPrice,
                products = products.map { it.toProduct() },
            )
        }

        @Serializable
        data class Product(
            @SerialName("cover_picture")
            val imageUrl: String? = null,

            @SerialName("quantity")
            val count: Int? = null,
        ) {
            fun toProduct(): OrderItem.Product {
                checkNotNull(imageUrl) { "imageUrl is null" }
                checkNotNull(count) { "count is null" }
                return OrderItem.Product(
                    imageUrl = Url(imageUrl),
                    count = count,
                )
            }
        }
    }
}
