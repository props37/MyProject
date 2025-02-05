package ru.livetyping.zarina.data.order.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.domain.model.order.OrderShort
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.data.order.impl.remote.api.OrderApi
import javax.inject.Inject

internal class OrderRemoteDataSourceImpl @Inject constructor(
    private val api: OrderApi,
) : OrderRemoteDataSource {
    override fun getOrderPageFlow(page: Int): Flow<Page<List<OrderShort>>> = flow {
        val orderPage = api.getOrders(page).toOrderPage()
        emit(orderPage)
    }

    override fun getOrderFlow(orderId: Order.Id): Flow<OrderDetailed> = flow {
        val order = api.getOrder(orderId).first().toOrderDetailed()
        emit(order)
    }

    override suspend fun cancelOrder(orderId: Order.Id) {
        api.cancelOrder(orderId)
    }
}
