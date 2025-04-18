package ru.livetyping.zarina.core.domain.usecase.category

import ru.livetyping.zarina.core.domain.model.category.CategoryPath
import ru.livetyping.zarina.core.domain.repository.CategoryRepository
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoryPathUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCategoryPathUseCaseImpl(
    private val categoryRepository: CategoryRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, CategoryPath>(logger), GetCategoryPathUseCase {

    override suspend fun execute(params: Params): CategoryPath {
        val categoryPath =
            categoryRepository.getCategoryPath(params.categoryId, params.cachePolicy)
        return categoryPath ?: error("CategoryPath not found for category ${params.categoryId}")
    }

    override suspend fun invoke(params: Params): Result<CategoryPath> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetCategoryPathUseCaseImpl"
    }
}
