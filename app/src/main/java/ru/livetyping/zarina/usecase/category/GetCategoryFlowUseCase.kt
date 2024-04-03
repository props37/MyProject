package ru.livetyping.zarina.usecase.category

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.category.CategoryRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.category.Category
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
