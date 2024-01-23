package ru.zarina.zarina.usecase.rework.product

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.product.ProductRepository
import ru.zarina.zarina.data.rework.product.pagination.FilteredProductPagingSource
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.common.Sorting
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.usecase.base.BasicUseCase
import javax.inject.Inject

class GetProductPagingDataFlowUseCase @Inject constructor(
    private val productRepository: ProductRepository,
) : BasicUseCase<GetProductPagingDataFlowUseCase.Params, Flow<PagingData<Product>>> {

    override fun invoke(params: Params): Flow<PagingData<Product>> {
        return Pager(
            config = getPagingConfig(),
            pagingSourceFactory = {
                FilteredProductPagingSource(
                    categoryId = params.categoryId,
                    sorting = params.sorting,
                    productRepository = productRepository,
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
        val sorting: Sorting,
    )

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = PAGE_SIZE
        private const val INITIAL_LOAD_SIZE = PAGE_SIZE * 2
        private const val MAX_SIZE = 300
    }
}
