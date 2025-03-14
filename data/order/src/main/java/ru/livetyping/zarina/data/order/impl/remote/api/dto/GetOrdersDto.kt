package ru.livetyping.zarina.data.order.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderShort
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.core.network.zarina.dto.PaginationInfoDto
import java.math.BigDecimal
import java.time.LocalDate

@Serializable
internal data class GetOrdersDto(
    @SerialName("items")
    val items: List<OrderDto>? = null,

    @SerialName("pagination")
    val pagination: PaginationInfoDto? = null,
) {
    fun toOrderPage(): Page<List<OrderShort>> {
        checkPropertyNotNull(items) { ::items }
        checkPropertyNotNull(pagination) { ::pagination }
        return Page(
            data = items.map { it.toOrderItem() },
            paginationInfo = pagination.toPaginationInfo(Int.MAX_VALUE),
        )
    }

    @Serializable
    data class OrderDto(
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
        val totalSum: Float? = null,

        @SerialName("products")
        val products: List<ProductDto>? = null,
    ) {
        fun toOrderItem(): OrderShort {
            checkPropertyNotNull(id) { ::id }
            checkPropertyNotNull(number) { ::number }
            checkPropertyNotNull(productCount) { ::productCount }
            checkPropertyNotNull(date) { ::date }
            checkPropertyNotNull(status) { ::status }
            checkPropertyNotNull(totalSum) { ::totalSum }
            checkPropertyNotNull(products) { ::products }
            return OrderShort(
                id = Order.Id(id.toString()),
                number = Order.Number(number),
                productCount = productCount,
                date = LocalDate.parse(date),
                status = status.toOrderStatus(),
                totalPrice = BigDecimal(totalSum.toDouble()),
                products = products.map { it.toProduct() },
            )
        }

        @Serializable
        data class ProductDto(
            @SerialName("cover_picture")
            val coverPicture: String? = null,

            @SerialName("quantity")
            val quantity: Int? = null,
        ) {
            fun toProduct(): OrderShort.Product {
                checkPropertyNotNull(coverPicture) { ::coverPicture }
                checkPropertyNotNull(quantity) { ::quantity }
                return OrderShort.Product(
                    imageUrl = Url.create(coverPicture),
                    count = quantity,
                )
            }
        }
    }
}
