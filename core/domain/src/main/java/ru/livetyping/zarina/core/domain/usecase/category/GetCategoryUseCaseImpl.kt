package ru.livetyping.zarina.core.domain.usecase.category

import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.repository.CategoryRepository
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoryUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCategoryUseCaseImpl(
    private val categoryRepository: CategoryRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Category>(logger), GetCategoryUseCase {

    override suspend fun execute(params: Params): Category {
        val category = categoryRepository.getCategoryFlow(params.id, params.cachePolicy)
            .firstOrNull()
        checkNotNull(category) { "Category is null" }
        return category
    }

    override suspend fun invoke(params: Params): Result<Category> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetCategoryFlowUseCaseImpl"
    }
}
