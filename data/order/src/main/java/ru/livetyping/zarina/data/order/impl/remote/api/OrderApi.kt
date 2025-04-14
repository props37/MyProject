package ru.livetyping.zarina.data.order.impl.remote.api

import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.network.zarina.dto.OrderDto
import ru.livetyping.zarina.data.order.impl.remote.api.dto.GetOrdersDto

internal interface OrderApi {
    suspend fun getOrders(page: Int): GetOrdersDto

    suspend fun getOrder(orderId: Order.Id): List<OrderDto>

    suspend fun cancelOrder(orderId: Order.Id)
}
