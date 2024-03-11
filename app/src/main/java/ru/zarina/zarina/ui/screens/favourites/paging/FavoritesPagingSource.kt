package ru.zarina.zarina.ui.screens.favourites.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.usecase.old.favorites.GetFavoritesPageUseCase

class FavouritesPagingSource(
    private val getFavoritesPageUseCase: GetFavoritesPageUseCase,
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val nextPageIndex = params.key ?: 0
        getFavoritesPageUseCase(
            GetFavoritesPageUseCase.Params(
                pageIndex = nextPageIndex
            )
        )
            .onSuccess {
                return LoadResult.Page(
                    data = it.value,
                    prevKey = it.pagination.previousPageIndex,
                    nextKey = it.pagination.nextPageIndex
                )
            }
        // TODO error handling
        return LoadResult.Error(NotImplementedError())
    }
}
