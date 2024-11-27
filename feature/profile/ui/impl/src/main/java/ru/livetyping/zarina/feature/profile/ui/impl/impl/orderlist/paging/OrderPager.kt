package ru.livetyping.zarina.feature.profile.ui.impl.impl.orderlist.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.order.OrderShort
import javax.inject.Inject
import javax.inject.Provider

internal class OrderPager @Inject constructor(
    private val orderPagingSource: Provider<OrderPagingSource>,
) {
    fun getOrderPagingDataFlow(): Flow<PagingData<OrderShort>> {
        return Pager(
            config = getPagingConfig(),
            pagingSourceFactory = { orderPagingSource.get() },
        ).flow
    }

    private fun getPagingConfig(): PagingConfig {
        return PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PREFETCH_DISTANCE,
            enablePlaceholders = false,
            initialLoadSize = INITIAL_LOAD_SIZE,
            maxSize = MAX_SIZE,
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = PAGE_SIZE
        private const val INITIAL_LOAD_SIZE = PAGE_SIZE * 2
        private const val MAX_SIZE = 100
    }
}