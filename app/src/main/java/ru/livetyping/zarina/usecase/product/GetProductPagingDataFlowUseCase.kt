package ru.livetyping.zarina.usecase.product

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.BasicUseCase
import ru.livetyping.zarina.data.product.ProductRepository
import ru.livetyping.zarina.data.product.pagination.ProductPagingSource
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.product.Product
import timber.log.Timber
import javax.inject.Inject

// TODO: [Medium] Move logic to repository

class GetProductPagingDataFlowUseCase @Inject constructor(
    private val productRepository: ProductRepository,
) : BasicUseCase<GetProductPagingDataFlowUseCase.Params, Flow<PagingData<Product>>> {

    override fun invoke(params: Params): Flow<PagingData<Product>> {
        return Pager(
            config = getPagingConfig(),
            pagingSourceFactory = {
                ProductPagingSource(
                    categoryId = params.categoryId,
                    filters = params.filters,
                    sorting = params.sorting,
                    productRepository = productRepository,
                    onAvailableFiltersReceived = {
                        Timber.tag(TAG).v("Available filters received: $it")
                        params.onAvailableFiltersReceived(it)
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

    data class Params(
        val categoryId: Category.Id,
        val filters: Filters?,
        val sorting: Sorting,
        val onAvailableFiltersReceived: (Filters) -> Unit,
    )

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = PAGE_SIZE
        private const val INITIAL_LOAD_SIZE = PAGE_SIZE * 2
        private const val MAX_SIZE = 300

        private const val TAG = "GetProductPagingDataFlowUseCase"
    }
}
