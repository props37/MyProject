package ru.livetyping.zarina.core.domain.usecase.product

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.category.CategoryInfo
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetCategoryInfoFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<CategoryInfo>>

    public data class Params(
        val categoryId: Category.Id,
        val filters: ProductFilters?,
    )

    public companion object {
        public fun getInstance(
            productRepository: ProductRepository,
            logger: UseCaseLogger?,
        ): GetCategoryInfoFlowUseCase {
            return GetCategoryInfoFlowUseCaseImpl(
                productRepository = productRepository,
                logger = logger,
            )
        }
    }
}
