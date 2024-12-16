package ru.livetyping.zarina.data.order.impl.remote.api

import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.data.order.impl.remote.api.dto.GetOrdersDto
import ru.livetyping.zarina.data.order.impl.remote.api.dto.OrderDto

internal interface OrderApi {
    suspend fun getOrders(page: Int): GetOrdersDto

    suspend fun getOrder(orderId: Order.Id): List<OrderDto>
}
