package ru.livetyping.zarina.ui.screen.myorders.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.order.OrderRepository
import ru.livetyping.zarina.data.order.pagination.OrderPagingSource
import ru.livetyping.zarina.domain.order.OrderItem
import javax.inject.Inject

class OrderPager @Inject constructor(
    private val orderRepository: OrderRepository,
) {
    fun getOrderPagingDataFlow(): Flow<PagingData<OrderItem>> {
        return Pager(
            config = getPagingConfig(),
            pagingSourceFactory = {
                OrderPagingSource(orderRepository)
            },
        ).flow
    }

    private fun getPagingConfig(): PagingConfig {
        return PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PREFETCH_DISTANCE,
            enablePlaceholders = true,
            initialLoadSize = INITIAL_LOAD_SIZE,
            maxSize = MAX_SIZE,
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = PAGE_SIZE
        private const val INITIAL_LOAD_SIZE = PAGE_SIZE * 2
        private const val MAX_SIZE = 100

        private const val TAG = "OrderPager"
    }
}
