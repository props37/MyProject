package ru.livetyping.zarina.data.product.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import ru.livetyping.zarina.data.product.ProductRepository
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.product.ProductItem
import timber.log.Timber

class ProductPagingSource(
    private val categoryId: Category.Id,
    private val filters: Filters?,
    private val sorting: Sorting,
    private val productRepository: ProductRepository,
    private val onAvailableFiltersReceived: (Filters) -> Unit,
) : PagingSource<Int, ProductItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductItem> {
        try {
            val page = params.key ?: 1
            val productsWithFiltersPage =
                productRepository.getProductsWithFiltersPageFlow(categoryId, filters, sorting, page)
                    .first()
            val products = productsWithFiltersPage.data.products
            onAvailableFiltersReceived(productsWithFiltersPage.data.filters)

            val paginationInfo = productsWithFiltersPage.paginationInfo
            val prevPage = paginationInfo.currentPage - 1
            val nextPage = paginationInfo.currentPage + 1
            val prevKey = prevPage.takeIf { it >= 1 }
            val nextKey = nextPage.takeIf { it <= paginationInfo.pageCount }
            val itemsBefore = (paginationInfo.currentPage - 1) * paginationInfo.pageSize
            val itemsAfter = paginationInfo.itemCount - (itemsBefore + products.size)
            return LoadResult.Page(
                data = products,
                prevKey = prevKey,
                nextKey = nextKey,
                itemsBefore = itemsBefore,
                itemsAfter = itemsAfter,
            )
        } catch (e: Exception) {
            Timber.e(e)
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
