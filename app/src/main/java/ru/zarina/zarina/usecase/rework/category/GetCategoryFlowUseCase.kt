package ru.zarina.zarina.usecase.rework.category

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.base.usecase.FlowUseCase
import ru.zarina.zarina.data.category.CategoryRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.rework.category.Category
import javax.inject.Inject

class GetCategoryFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val categoryRepository: CategoryRepository,
) : FlowUseCase<GetCategoryFlowUseCase.Params, Category>(dispatcher) {

    override fun execute(params: Params): Flow<Category> {
        return categoryRepository.getCategoryFlow(params.id)
    }

    data class Params(val id: Category.Id)
}
