package ru.zarina.zarina.ui.screens.catalog.products.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.MutableStateFlow
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.ProductSort
import ru.zarina.zarina.usecase.catalog.GetProductsPageUseCase

class CategoryProductPagingSource(
    private val category: Category,
    private val sort: ProductSort,
    private val filtration: Filtration?,
    private val getProductsPageUseCase: GetProductsPageUseCase,
) : PagingSource<Int, Product>() {

    val itemCount = MutableStateFlow<Int?>(null)
    val appliedFiltration = MutableStateFlow<Filtration?>(null)

    override fun getRefreshKey(
        state: PagingState<Int, Product>,
    ): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(
        params: LoadParams<Int>,
    ): LoadResult<Int, Product> {
        val nextPageIndex = params.key ?: 0
        getProductsPageUseCase(
            GetProductsPageUseCase.Params(
                category = category,
                sort = sort,
                filtration = filtration,
                pageIndex = nextPageIndex
            )
        )
            .onSuccess {
                itemCount.value = it.pagination.totalItemCount
                appliedFiltration.value = it.value.filtration
                return LoadResult.Page(
                    data = it.value.products,
                    prevKey = it.pagination.previousPageIndex,
                    nextKey = it.pagination.nextPageIndex
                )
            }
        // TODO error handling
        return LoadResult.Error(NotImplementedError())
    }

}
