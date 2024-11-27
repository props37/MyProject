package ru.livetyping.zarina.data.order.impl.remote.api

import ru.livetyping.zarina.data.order.impl.remote.api.dto.GetOrdersDto

internal interface OrderApi {
    suspend fun getOrders(page: Int): GetOrdersDto
}
