package ru.zarina.zarina.data.rework.product.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import ru.zarina.zarina.data.rework.product.ProductRepository
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.common.Sorting
import ru.zarina.zarina.domain.rework.product.Product

class FilteredProductPagingSource(
    private val categoryId: Category.Id,
    private val sorting: Sorting,
    private val productRepository: ProductRepository,
) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        try {
            val page = params.key ?: 1
            val filteredProductPage =
                productRepository.getFilteredProductPageFlow(categoryId, page, sorting).first()
            val products = filteredProductPage.data.products

            val paginationInfo = filteredProductPage.paginationInfo
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
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
