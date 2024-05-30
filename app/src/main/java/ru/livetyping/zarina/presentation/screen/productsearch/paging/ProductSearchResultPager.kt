package ru.livetyping.zarina.presentation.screen.productsearch.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.productsearch.ProductSearchRepository
import ru.livetyping.zarina.data.productsearch.pagination.ProductSearchResultPagingSource
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.domain.product.ProductItem
import javax.inject.Inject

class ProductSearchResultPager @Inject constructor(
    private val productSearchRepository: ProductSearchRepository,
) {
    fun getProductPagingDataFlow(
        query: String,
        sorting: Sorting,
    ): Flow<PagingData<ProductItem>> {
        return Pager(
            config = getPagingConfig(),
            pagingSourceFactory = {
                ProductSearchResultPagingSource(
                    query = query,
                    sorting = sorting,
                    productSearchRepository = productSearchRepository,
                )
            }
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
    }
}
