package ru.livetyping.zarina.feature.profile.ui.impl.impl.orderlist.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.core.domain.model.order.OrderShort
import ru.livetyping.zarina.core.domain.usecase.order.GetOrderPageFlowUseCase
import timber.log.Timber
import javax.inject.Inject

internal class OrderPagingSource @Inject constructor(
    private val getOrderPageFlow: GetOrderPageFlowUseCase,
) : PagingSource<Int, OrderShort>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OrderShort> {
        return try {
            val page = params.key ?: 1
            val orderPageParams = GetOrderPageFlowUseCase.Params(page)
            val orderPageResult = getOrderPageFlow(orderPageParams).firstOrNull()
            checkNotNull(orderPageResult) { "Failed to get orders" }

            val orderPage = orderPageResult.getOrThrow()
            val orders = orderPage.data
            val paginationInfo = orderPage.paginationInfo

            val prevPage = paginationInfo.currentPage - 1
            val nextPage = paginationInfo.currentPage + 1
            val prevKey = prevPage.takeIf { it >= 1 }
            val nextKey = nextPage.takeIf { it <= paginationInfo.pageCount }
            LoadResult.Page(
                data = orders,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, OrderShort>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        private const val TAG = "OrderPagingSource"
    }
}
