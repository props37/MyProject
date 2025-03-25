package ru.livetyping.zarina.data.order

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.order.remote.OrderRemoteDataSource
import ru.livetyping.zarina.domain.common.Page
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.domain.order.OrderCreationParams
import ru.livetyping.zarina.domain.order.OrderDetails
import ru.livetyping.zarina.domain.order.OrderItem
import ru.livetyping.zarina.domain.order.OrderStatus
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

    suspend fun getOrderStatus(orderId: Order.Id): OrderStatus {
        return remoteDataSource.getOrderStatus(orderId)
    }

    suspend fun createOrder(params: OrderCreationParams): OrderDetails {
        return remoteDataSource.createOrder(params)
    }

    suspend fun cancelOrder(orderId: Order.Id) {
        remoteDataSource.cancelOrder(orderId)
    }
}
