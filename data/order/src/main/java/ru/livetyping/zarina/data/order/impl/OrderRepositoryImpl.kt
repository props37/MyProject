package ru.livetyping.zarina.data.order.impl

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.domain.model.order.OrderShort
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.repository.OrderRepository
import ru.livetyping.zarina.data.order.impl.remote.OrderRemoteDataSource
import javax.inject.Inject

internal class OrderRepositoryImpl @Inject constructor(
    private val remoteDataSource: OrderRemoteDataSource,
) : OrderRepository {
    override fun getOrderPageFlow(page: Int): Flow<Page<List<OrderShort>>> {
        return remoteDataSource.getOrderPageFlow(page)
    }

    override fun getOrderFlow(orderId: Order.Id): Flow<OrderDetailed> {
        return remoteDataSource.getOrderFlow(orderId)
    }

    override suspend fun cancelOrder(orderId: Order.Id) {
        remoteDataSource.cancelOrder(orderId)
    }
}
