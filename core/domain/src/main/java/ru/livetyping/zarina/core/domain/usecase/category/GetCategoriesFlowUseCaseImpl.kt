package ru.livetyping.zarina.core.domain.usecase.category

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Categories
import ru.livetyping.zarina.core.domain.repository.CategoryRepository
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCategoriesFlowUseCaseImpl(
    private val categoryRepository: CategoryRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Unit, Categories>(logger), GetCategoriesFlowUseCase {

    override fun execute(params: Unit): Flow<Categories> {
        return categoryRepository.getCategoriesFlow()
    }

    override fun invoke(): Flow<Result<Categories>> {
        return call(Unit)
    }
}
