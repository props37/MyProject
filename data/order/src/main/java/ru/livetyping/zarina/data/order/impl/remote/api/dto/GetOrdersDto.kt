package ru.livetyping.zarina.data.order.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderShort
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.core.network.zarina.dto.OrderStatusDto
import ru.livetyping.zarina.core.network.zarina.dto.PaginationInfoDto
import timber.log.Timber
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
            data = items.mapNotNull { it.toOrderItem() },
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
        fun toOrderItem(): OrderShort? {
            return if (
                id != null
                && number != null
                && productCount != null
                && date != null
                && status != null
                && totalSum != null
                && products != null
            ) {
                OrderShort(
                    id = Order.Id(id.toString()),
                    number = Order.Number(number),
                    productCount = productCount,
                    date = LocalDate.parse(date),
                    status = status.toOrderStatus(),
                    totalPrice = BigDecimal(totalSum.toDouble()),
                    products = products.mapNotNull { it.toProduct() },
                )
            } else {
                Timber.tag(TAG).e("Ignore $this because it can't be mapped to OrderShort")
                null
            }
        }

        @Serializable
        data class ProductDto(
            @SerialName("cover_picture")
            val coverPicture: String? = null,

            @SerialName("quantity")
            val quantity: Int? = null,
        ) {
            fun toProduct(): OrderShort.Product? {
                return if (coverPicture != null && quantity != null) {
                    OrderShort.Product(
                        imageUrl = Url.create(coverPicture),
                        count = quantity,
                    )
                } else {
                    Timber.tag(TAG).e("Ignore $this because it can't be mapped to OrderShort.Product")
                    null
                }
            }
        }
    }

    private companion object {
        private const val TAG = "GetOrdersDto"
    }
}
