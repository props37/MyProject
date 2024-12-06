package ru.livetyping.zarina.feature.productlist.ui.impl.impl.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.usecase.product.GetProductsWithFiltersPageFlowUseCase
import timber.log.Timber
import javax.inject.Inject

internal class ProductPager @Inject constructor(
    private val getProductsWithFiltersPageFlowUseCase: GetProductsWithFiltersPageFlowUseCase,
) {
    fun getProductPagingDataFlow(
        categoryId: Category.Id,
        filters: ProductFilters?,
        sorting: ProductSorting,
        onAvailableFiltersReceived: (ProductFilters) -> Unit,
    ): Flow<PagingData<ProductShort>> {
        return Pager(
            config = getPagingConfig(),
            pagingSourceFactory = {
                ProductPagingSource(
                    categoryId = categoryId,
                    filters = filters,
                    sorting = sorting,
                    getProductsWithFiltersPageFlowUseCase = getProductsWithFiltersPageFlowUseCase,
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

        private const val TAG = "ProductPager"
    }
}
