package ru.livetyping.zarina.feature.productlist.ui.impl.impl.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.usecase.product.GetProductsWithFiltersPageFlowUseCase
import timber.log.Timber

internal class ProductPagingSource(
    private val categoryId: Category.Id,
    private val filters: ProductFilters?,
    private val sorting: ProductSorting,
    private val getProductsWithFiltersPageFlowUseCase: GetProductsWithFiltersPageFlowUseCase,
    private val onAvailableFiltersReceived: (ProductFilters) -> Unit,
) : PagingSource<Int, ProductShort>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductShort> {
        try {
            val page = params.key ?: 1
            val productsWithFiltersPageParams = GetProductsWithFiltersPageFlowUseCase.Params(
                categoryId = categoryId,
                filters = filters,
                sorting = sorting,
                page = page,
            )
            val productsWithFiltersPageResult =
                getProductsWithFiltersPageFlowUseCase(productsWithFiltersPageParams).firstOrNull()
            checkNotNull(productsWithFiltersPageResult) { "Failed to get products" }

            val productsWithFiltersPage = productsWithFiltersPageResult.getOrThrow()
            val products = productsWithFiltersPage.data.products
            onAvailableFiltersReceived(productsWithFiltersPage.data.filters)

            val paginationInfo = productsWithFiltersPage.paginationInfo
            val prevPage = paginationInfo.currentPage - 1
            val nextPage = paginationInfo.currentPage + 1
            val prevKey = prevPage.takeIf { it >= 1 }
            val nextKey = nextPage.takeIf { it <= paginationInfo.pageCount }
            val itemsBefore = ((paginationInfo.currentPage - 1) * paginationInfo.pageSize)
                .coerceAtLeast(0)
            val itemsAfter = (paginationInfo.itemTotalCount - (itemsBefore + products.size))
                .coerceAtLeast(0)
            return LoadResult.Page(
                data = products,
                prevKey = prevKey,
                nextKey = nextKey,
                itemsBefore = itemsBefore,
                itemsAfter = itemsAfter,
            )
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductShort>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        private const val TAG = "ProductPagingSource"
    }
}
