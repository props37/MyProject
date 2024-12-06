package ru.livetyping.zarina.feature.wishlist.ui.impl.impl.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductPageFlowUseCase
import timber.log.Timber
import javax.inject.Inject

internal class WishlistProductPagingSource @Inject constructor(
    private val getWishlistProductPageFlow: GetWishlistProductPageFlowUseCase,
) : PagingSource<Int, ProductShort>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductShort> {
        return try {
            val page = params.key ?: 1
            val wishlistProductPageParams = GetWishlistProductPageFlowUseCase.Params(page)
            val wishlistProductPageResult =
                getWishlistProductPageFlow(wishlistProductPageParams).firstOrNull()
            checkNotNull(wishlistProductPageResult) { "Failed to get wishlist product IDs" }

            val wishlistProductPage = wishlistProductPageResult.getOrThrow()
            val products = wishlistProductPage.data
            val paginationInfo = wishlistProductPage.paginationInfo

            val prevPage = paginationInfo.currentPage - 1
            val nextPage = paginationInfo.currentPage + 1
            val prevKey = prevPage.takeIf { it >= 1 }
            val nextKey = nextPage.takeIf { it <= paginationInfo.pageCount }
            val itemsBefore = ((paginationInfo.currentPage - 1) * paginationInfo.pageSize)
                .coerceAtLeast(0)
            val itemsAfter = (paginationInfo.itemTotalCount - (itemsBefore + products.size))
                .coerceAtLeast(0)
            LoadResult.Page(
                data = products,
                prevKey = prevKey,
                nextKey = nextKey,
                itemsBefore = itemsBefore,
                itemsAfter = itemsAfter,
            )
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductShort>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val TAG = "WishlistProductPagingSource"
    }
}
