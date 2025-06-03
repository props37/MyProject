package ru.livetyping.zarina.core.domain.usecase.product

import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.filter.ProductsWithFilters
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.domain.usecase.product.GetProductsWithFiltersPageUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetProductsWithFiltersPageUseCaseImpl(
    private val productRepository: ProductRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Page<ProductsWithFilters>>(logger), GetProductsWithFiltersPageUseCase {

    override suspend fun execute(params: Params): Page<ProductsWithFilters> {
        return productRepository.getProductsWithFiltersPage(
            categoryId = params.categoryId,
            filters = params.filters,
            sorting = params.sorting,
            page = params.page,
            pageSize = params.pageSize,
        )
    }

    override suspend fun invoke(params: Params): Result<Page<ProductsWithFilters>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "getProductsWithFiltersPageFlowUseCaseImpl"
    }
}
