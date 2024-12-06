package ru.livetyping.zarina.core.domain.usecase.product

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.product.filter.ProductsWithFilters
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetProductsWithFiltersPageFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<Page<ProductsWithFilters>>>

    public data class Params(
        val categoryId: Category.Id,
        val filters: ProductFilters?,
        val sorting: ProductSorting,
        val page: Int,
    )

    public companion object {
        public fun getInstance(
            productRepository: ProductRepository,
            logger: UseCaseLogger?,
        ): GetProductsWithFiltersPageFlowUseCase {
            return GetProductsWithFiltersPageFlowUseCaseImpl(
                productRepository = productRepository,
                logger = logger,
            )
        }
    }
}
