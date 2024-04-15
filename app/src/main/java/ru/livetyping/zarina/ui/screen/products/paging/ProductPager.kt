package ru.livetyping.zarina.ui.screen.products.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.product.ProductRepository
import ru.livetyping.zarina.data.product.pagination.ProductPagingSource
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.product.Product
import timber.log.Timber
import javax.inject.Inject

class ProductPager @Inject constructor(
    private val productRepository: ProductRepository,
) {
    fun getProductPagingDataFlow(
        categoryId: Category.Id,
        filters: Filters?,
        sorting: Sorting,
        onAvailableFiltersReceived: (Filters) -> Unit,
    ): Flow<PagingData<Product>> {
        return Pager(
            config = getPagingConfig(),
            pagingSourceFactory = {
                ProductPagingSource(
                    categoryId = categoryId,
                    filters = filters,
                    sorting = sorting,
                    productRepository = productRepository,
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
            maxSize = MAX_SIZE,
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = PAGE_SIZE
        private const val INITIAL_LOAD_SIZE = PAGE_SIZE * 2
        private const val MAX_SIZE = 300

        private const val TAG = "ProductsPager"
    }
}
