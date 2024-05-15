package ru.livetyping.zarina.data.favorite.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import ru.livetyping.zarina.data.favorite.FavoriteRepository
import ru.livetyping.zarina.domain.product.ProductItem
import timber.log.Timber

class FavoriteProductPagingSource(
    private val favoriteRepository: FavoriteRepository,
) : PagingSource<Int, ProductItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductItem> {
        return try {
            val page = params.key ?: 1
            val favoriteProductPage = favoriteRepository.getFavoriteProductPageFlow(page).first()
            val products = favoriteProductPage.data

            val paginationInfo = favoriteProductPage.paginationInfo
            val prevPage = paginationInfo.currentPage - 1
            val nextPage = paginationInfo.currentPage + 1
            val prevKey = prevPage.takeIf { it >= 1 }
            val nextKey = nextPage.takeIf { it <= paginationInfo.pageCount }
            val itemsBefore = (paginationInfo.currentPage - 1) * paginationInfo.pageSize
            val itemsAfter = paginationInfo.itemCount - (itemsBefore + products.size)
            LoadResult.Page(
                data = products,
                prevKey = prevKey,
                nextKey = nextKey,
                itemsBefore = itemsBefore,
                itemsAfter = itemsAfter,
            )
        } catch (e: Exception) {
            Timber.e(e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
