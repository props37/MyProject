package ru.livetyping.zarina.core.domain.usecase.product

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.filter.ProductsWithFilters
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.domain.usecase.product.GetProductsWithFiltersPageFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetProductsWithFiltersPageFlowUseCaseImpl(
    private val productRepository: ProductRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, Page<ProductsWithFilters>>(logger), GetProductsWithFiltersPageFlowUseCase {

    override fun execute(params: Params): Flow<Page<ProductsWithFilters>> {
        return productRepository.getProductsWithFiltersPageFlow(
            categoryId = params.categoryId,
            filters = params.filters,
            sorting = params.sorting,
            page = params.page,
        )
    }

    override fun invoke(params: Params): Flow<Result<Page<ProductsWithFilters>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "getProductsWithFiltersPageFlowUseCaseImpl"
    }
}
