package ru.livetyping.zarina.data.order.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.order.remote.api.OrderApi
import ru.livetyping.zarina.domain.common.Page
import ru.livetyping.zarina.domain.order.OrderItem
import javax.inject.Inject

class OrderRemoteDataSource @Inject constructor(
    private val api: OrderApi,
) {
    fun getOrderPageFlow(page: Int): Flow<Page<List<OrderItem>>> = flow {
        val orderPage = api.getOrders(page).toOrderPage()
        emit(orderPage)
    }
}
