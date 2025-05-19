package ru.livetyping.zarina.core.domain.usecase.category

import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.repository.CategoryRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetCategoryUseCase {
    public suspend operator fun invoke(params: Params): Result<Category>

    public data class Params(
        val id: Category.Id,
        val cachePolicy: CachePolicy,
    )

    public companion object {
        public fun getInstance(
            categoryRepository: CategoryRepository,
            logger: UseCaseLogger?,
        ): GetCategoryUseCase {
            return GetCategoryUseCaseImpl(
                categoryRepository = categoryRepository,
                logger = logger,
            )
        }
    }
}
