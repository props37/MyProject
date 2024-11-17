package ru.livetyping.zarina.core.domain.usecase.category

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Categories
import ru.livetyping.zarina.core.domain.repository.CategoryRepository
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoriesFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCategoriesFlowUseCaseImpl(
    private val categoryRepository: CategoryRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, Categories>(logger), GetCategoriesFlowUseCase {

    override fun execute(params: Params): Flow<Categories> {
        return categoryRepository.getCategoriesFlow(params.cachePolicy)
    }

    override fun invoke(params: Params): Flow<Result<Categories>> {
        return call(params)
    }
}
