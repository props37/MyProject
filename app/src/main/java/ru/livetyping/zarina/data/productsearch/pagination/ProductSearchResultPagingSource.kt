package ru.livetyping.zarina.data.productsearch.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import ru.livetyping.zarina.data.productsearch.ProductSearchRepository
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.product.ProductItem
import timber.log.Timber

class ProductSearchResultPagingSource(
    private val query: String,
    private val sorting: Sorting,
    private val productSearchRepository: ProductSearchRepository,
    private val onAvailableFiltersReceived: (Filters) -> Unit,
) : PagingSource<Int, ProductItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductItem> {
        try {
            val offset = params.key ?: 0
            val result = productSearchRepository.searchProductsFlow(query, sorting, offset).first()
            val products = result.products
            onAvailableFiltersReceived(result.availableFilters)

            val nextOffset = result.offset + products.size
            val nextKey = nextOffset.takeIf { products.isNotEmpty() }
            val itemsAfter = result.productTotalCount - (products.size + offset)
            return LoadResult.Page(
                data = products,
                prevKey = null, // TODO: [Low] Implement
                nextKey = nextKey,
                itemsBefore = offset,
                itemsAfter = itemsAfter,
            )
        } catch (e: Exception) {
            Timber.e(e)
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductItem>): Int? = null
}
