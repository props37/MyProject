package ru.zarina.zarina.ui.screens.catalog.products.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import ru.zarina.zarina.domain.old.FilteredProducts
import ru.zarina.zarina.domain.old.Filtration
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.ui.common.base.paging.PageHolder
import ru.zarina.zarina.usecase.old.favorites.GetFavoriteIdsUseCase
import ru.zarina.zarina.util.base.usecase.invoke

class CategoryProductPagingSource(
    private val pageHolder: PageHolder<FilteredProducts>,
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
) : PagingSource<Int, Product>() {

    val itemCount = MutableStateFlow<Int?>(null)
    val appliedFiltration = MutableStateFlow<Filtration?>(null)

    override fun getRefreshKey(
        state: PagingState<Int, Product>,
    ): Int? {
        val anchorPosition = state.anchorPosition ?: return 0
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return 0
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(
        params: LoadParams<Int>,
    ): LoadResult<Int, Product> {
        val pageIndex = params.key ?: -1
        val page = pageHolder.getPage(pageIndex)

        if (page == null) {
            val previousKey = (pageIndex - 1).takeIf { pageHolder.getPage(it) != null }
            val nextKey = (pageIndex + 1).takeIf { pageHolder.getPage(it) != null }
            return LoadResult.Page(
                data = emptyList(),
                prevKey = previousKey,
                nextKey = nextKey,
            )
        }

        itemCount.value = page.pagination.totalItemCount
        appliedFiltration.value = page.value.filtration

        val favoriteIds = getFavoriteIdsUseCase().first().getOrNull() ?: emptySet()

        val products = page.value.products
            .map { product ->
                val isFavorite = favoriteIds.contains(product.id)
                if (isFavorite != product.isFavorite) {
                    product.copy(isFavorite = isFavorite)
                } else {
                    product
                }
            }

        val previousKey =
            page.pagination.previousPageIndex?.takeIf { pageHolder.getPage(it) != null }
        val nextKey = page.pagination.nextPageIndex?.takeIf { pageHolder.getPage(it) != null }

        return LoadResult.Page(
            data = products,
            prevKey = previousKey,
            nextKey = nextKey,
        )
    }

}
