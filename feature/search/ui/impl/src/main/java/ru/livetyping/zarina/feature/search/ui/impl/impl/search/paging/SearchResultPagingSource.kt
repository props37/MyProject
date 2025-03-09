package ru.livetyping.zarina.feature.search.ui.impl.impl.search.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.usecase.search.SearchFlowUseCase
import timber.log.Timber

internal class SearchResultPagingSource(
    private val query: String,
    private val sorting: ProductSorting,
    private val filters: ProductFilters?,
    private val searchFlowUseCase: SearchFlowUseCase,
    private val onAvailableFiltersReceived: (ProductFilters) -> Unit,
) : PagingSource<Int, ProductShort>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductShort> {
        return try {
            val offset = params.key ?: 0
            val searchParams = SearchFlowUseCase.Params(
                query = query,
                sorting = sorting,
                filters = filters,
                offset = offset,
            )
            val result = searchFlowUseCase(searchParams).first().getOrThrow()
            val products = result.products
            if (!result.availableFilters.isEmptyIgnoringSorting) {
                onAvailableFiltersReceived(result.availableFilters)
            }

            val nextOffset = result.offset + products.size
            val nextKey = nextOffset.takeIf { products.isNotEmpty() }
            val itemsAfter = (result.productTotalCount - (products.size + offset))
                .coerceAtLeast(0)
            return LoadResult.Page(
                data = products,
                prevKey = null, // TODO: [Low] Implement
                nextKey = nextKey,
                itemsBefore = offset.coerceAtLeast(0),
                itemsAfter = itemsAfter,
            )
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductShort>): Int? = null

    private companion object {
        const val TAG = "SearchResultPagingSource"
    }
}
