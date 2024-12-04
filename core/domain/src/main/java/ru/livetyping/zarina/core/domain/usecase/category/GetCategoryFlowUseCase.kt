package ru.livetyping.zarina.core.domain.usecase.category

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.repository.CategoryRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetCategoryFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<Category?>>

    public data class Params(
        val id: Category.Id,
        val cachePolicy: CachePolicy,
    )

    public companion object {
        public fun getInstance(
            categoryRepository: CategoryRepository,
            logger: UseCaseLogger?,
        ): GetCategoryFlowUseCase {
            return GetCategoryFlowUseCaseImpl(
                categoryRepository = categoryRepository,
                logger = logger,
            )
        }
    }
}
