package ru.livetyping.zarina.data.order.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.domain.model.order.OrderShort
import ru.livetyping.zarina.core.domain.model.pagination.Page

internal interface OrderRemoteDataSource {
    fun getOrderPageFlow(page: Int): Flow<Page<List<OrderShort>>>

    fun getOrderFlow(orderId: Order.Id): Flow<OrderDetailed>

    suspend fun cancelOrder(orderId: Order.Id)
}
