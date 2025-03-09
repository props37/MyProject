package ru.livetyping.zarina.feature.search.ui.impl.impl.search.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.usecase.search.SearchFlowUseCase
import timber.log.Timber
import javax.inject.Inject

internal class SearchResultPager @Inject constructor(
    private val searchFlowUseCase: SearchFlowUseCase,
) {
    fun getSearchResultPagingDataFlow(
        query: String,
        sorting: ProductSorting,
        filters: ProductFilters?,
        onAvailableFiltersReceived: (ProductFilters) -> Unit,
    ): Flow<PagingData<ProductShort>> {
        return Pager(
            config = getPagingConfig(),
            pagingSourceFactory = {
                SearchResultPagingSource(
                    query = query,
                    sorting = sorting,
                    filters = filters,
                    searchFlowUseCase = searchFlowUseCase,
                    onAvailableFiltersReceived = {
                        Timber.tag(TAG).v("Available filters received: $it")
                        onAvailableFiltersReceived(it)
                    },
                )
            },
        ).flow
    }

    private fun getPagingConfig(): PagingConfig {
        return PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PREFETCH_DISTANCE,
            enablePlaceholders = true,
            initialLoadSize = INITIAL_LOAD_SIZE,
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = PAGE_SIZE
        private const val INITIAL_LOAD_SIZE = PAGE_SIZE * 2

        private const val TAG = "SearchResultPager"
    }
}