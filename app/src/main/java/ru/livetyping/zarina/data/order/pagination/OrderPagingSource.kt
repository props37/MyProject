package ru.livetyping.zarina.data.order.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import ru.livetyping.zarina.data.order.OrderRepository
import ru.livetyping.zarina.domain.order.OrderItem

class OrderPagingSource(
    private val orderRepository: OrderRepository,
) : PagingSource<Int, OrderItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OrderItem> {
        try {
            val page = params.key ?: 1
            val orderPage = orderRepository.getOrderPageFlow(page).first()
            val orders = orderPage.data

            val paginationInfo = orderPage.paginationInfo
            val prevPage = paginationInfo.currentPage - 1
            val nextPage = paginationInfo.currentPage + 1
            val prevKey = prevPage.takeIf { it >= 1 }
            val nextKey = nextPage.takeIf { it <= paginationInfo.pageCount }
            return LoadResult.Page(
                data = orders,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, OrderItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
