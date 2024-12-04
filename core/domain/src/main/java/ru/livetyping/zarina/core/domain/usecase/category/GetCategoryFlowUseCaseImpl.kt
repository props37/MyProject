package ru.livetyping.zarina.core.domain.usecase.category

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.repository.CategoryRepository
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoryFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCategoryFlowUseCaseImpl(
    private val categoryRepository: CategoryRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, Category?>(logger), GetCategoryFlowUseCase {

    override fun execute(params: Params): Flow<Category?> {
        return categoryRepository.getCategoryFlow(params.id, params.cachePolicy)
    }

    override fun invoke(params: Params): Flow<Result<Category?>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetCategoryFlowUseCaseImpl"
    }
}
