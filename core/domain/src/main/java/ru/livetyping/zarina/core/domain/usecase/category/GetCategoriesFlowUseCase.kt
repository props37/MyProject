package ru.livetyping.zarina.core.domain.usecase.category

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Categories
import ru.livetyping.zarina.core.domain.repository.CategoryRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetCategoriesFlowUseCase {
    public operator fun invoke(): Flow<Result<Categories>>

    public companion object {
        public fun getInstance(
            categoryRepository: CategoryRepository,
            logger: UseCaseLogger?,
        ): GetCategoriesFlowUseCase {
            return GetCategoriesFlowUseCaseImpl(categoryRepository, logger)
        }
    }
}
