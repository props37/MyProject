package ru.livetyping.zarina.feature.wishlist.ui.impl.screen.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductPageUseCase
import timber.log.Timber
import javax.inject.Inject

internal class WishlistProductPagingSource @Inject constructor(
    private val getWishlistProductPage: GetWishlistProductPageUseCase,
) : PagingSource<Int, ProductShort>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductShort> {
        return try {
            val page = params.key ?: 1
            val wishlistProductPageParams = GetWishlistProductPageUseCase.Params(page)
            val wishlistProductPageResult = getWishlistProductPage(wishlistProductPageParams)

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
