package ru.livetyping.zarina.core.domain.usecase.category

import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.category.CategoryPath
import ru.livetyping.zarina.core.domain.repository.CategoryRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetCategoryPathUseCase {
    public suspend operator fun invoke(params: Params): Result<CategoryPath>

    public data class Params(
        val categoryId: Category.Id,
        val cachePolicy: CachePolicy,
    )

    public companion object {
        public fun getInstance(
            categoryRepository: CategoryRepository,
            logger: UseCaseLogger?,
        ): GetCategoryPathUseCase {
            return GetCategoryPathUseCaseImpl(
                categoryRepository = categoryRepository,
                logger = logger,
            )
        }
    }
}
