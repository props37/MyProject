package ru.livetyping.zarina.data.order.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.order.OrderShort
import ru.livetyping.zarina.core.domain.model.pagination.Page
import javax.inject.Inject

internal class OrderRemoteDataSourceImpl @Inject constructor(

) : OrderRemoteDataSource {
    override fun getOrderPageFlow(page: Int): Flow<Page<List<OrderShort>>> {
        TODO("Not yet implemented")
    }
}
