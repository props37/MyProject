package ru.livetyping.zarina.data.user.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.livetyping.zarina.domain.common.Page
import ru.livetyping.zarina.domain.user.LoyaltyProgramBonusAction
import timber.log.Timber

class LoyaltyProgramBonusActionPagingSource(
    private val fetchBonusActionPage: suspend (page: Int) -> Page<List<LoyaltyProgramBonusAction>>,
) : PagingSource<Int, LoyaltyProgramBonusAction>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LoyaltyProgramBonusAction> {
        return try {
            val page = params.key ?: 1
            val bonusActionPage = fetchBonusActionPage(page)
            val actions = bonusActionPage.data

            val paginationInfo = bonusActionPage.paginationInfo
            val prevPage = paginationInfo.currentPage - 1
            val nextPage = paginationInfo.currentPage + 1
            val prevKey = prevPage.takeIf { it >= 1 }
            val nextKey = nextPage.takeIf { it <= paginationInfo.pageCount }
            LoadResult.Page(
                data = actions,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (e: Exception) {
            Timber.e(e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LoyaltyProgramBonusAction>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
