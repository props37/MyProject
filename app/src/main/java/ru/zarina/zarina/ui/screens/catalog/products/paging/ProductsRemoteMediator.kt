package ru.zarina.zarina.ui.screens.catalog.products.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.FilteredProducts
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.ProductSort
import ru.zarina.zarina.ui.common.base.paging.PageHolder
import ru.zarina.zarina.usecase.old.catalog.GetProductsPageUseCase

@OptIn(ExperimentalPagingApi::class)
class ProductsRemoteMediator(
    private val pageHolder: PageHolder<FilteredProducts>,
    private val getProductsPageUseCase: GetProductsPageUseCase,
    private val category: Category,
    private val sort: ProductSort,
    private val filtration: Filtration?,
) : RemoteMediator<Int, Product>() {

    private val listeners = MutableStateFlow<Set<PagingSource<Int, Product>>>(emptySet())

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Product>,
    ): MediatorResult {
        val nextPageIndex = when (loadType) {
            LoadType.REFRESH -> state.anchorPosition ?: 0
            LoadType.PREPEND -> pageHolder.pages.value.minBy { it.key }.key - 1
            LoadType.APPEND -> pageHolder.pages.value.maxBy { it.key }.key + 1
        }

        if (nextPageIndex < 0) return MediatorResult.Success(endOfPaginationReached = true)

        val savedPage = pageHolder.getPage(nextPageIndex)

        if (savedPage == null) {
            getProductsPageUseCase(
                GetProductsPageUseCase.Params(
                    category = category,
                    sort = sort,
                    filtration = filtration,
                    pageIndex = nextPageIndex,
                )
            )
                .onSuccess { page ->
                    pageHolder.insert(page)
                    val isPaginationEndReached = when (loadType) {
                        LoadType.REFRESH -> false
                        LoadType.PREPEND -> page.pagination.currentPageIndex <= 0
                        LoadType.APPEND -> page.pagination.currentPageIndex + 1 >= page.pagination.pageCount
                    }
                    listeners.value.forEach {
                        it.invalidate()
                        removeListener(it)
                    }
                    return MediatorResult.Success(endOfPaginationReached = isPaginationEndReached)
                }
                .onFailure {
                    return MediatorResult.Error(it)
                }
            return MediatorResult.Error(IllegalStateException())
        } else {
            val isPaginationEndReached = when (loadType) {
                LoadType.REFRESH -> false
                LoadType.PREPEND -> savedPage.pagination.currentPageIndex <= 0
                LoadType.APPEND -> savedPage.pagination.currentPageIndex + 1 >= savedPage.pagination.pageCount
            }
            return MediatorResult.Success(endOfPaginationReached = isPaginationEndReached)
        }
    }

    fun addListener(pagingSource: PagingSource<Int, Product>) {
        listeners.update { it + pagingSource }
    }

    private fun removeListener(pagingSource: PagingSource<Int, Product>) {
        listeners.update { it - pagingSource }
    }
}
