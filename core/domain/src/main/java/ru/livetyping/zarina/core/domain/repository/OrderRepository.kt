package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.domain.model.order.OrderShort
import ru.livetyping.zarina.core.domain.model.pagination.Page

public interface OrderRepository {
    public fun getOrderPageFlow(page: Int): Flow<Page<List<OrderShort>>>

    public fun getOrderFlow(orderId: Order.Id): Flow<OrderDetailed>
}
