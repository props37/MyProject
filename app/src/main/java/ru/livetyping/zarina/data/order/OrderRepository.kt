package ru.livetyping.zarina.data.order

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.order.remote.OrderRemoteDataSource
import ru.livetyping.zarina.domain.common.Page
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.domain.order.OrderDetails
import ru.livetyping.zarina.domain.order.OrderItem
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val remoteDataSource: OrderRemoteDataSource,
) {
    fun getOrderPageFlow(page: Int): Flow<Page<List<OrderItem>>> {
        return remoteDataSource.getOrderPageFlow(page)
    }

    fun getOrderFlow(orderId: Order.Id): Flow<OrderDetails> {
        return remoteDataSource.getOrderFlow(orderId)
    }
}
