package ru.zarina.zarina.usecase.rework.category

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.category.CategoryRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.common.Categories
import ru.zarina.zarina.usecase.base.FlowUseCase
import javax.inject.Inject

class GetCategoriesFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val categoryRepository: CategoryRepository,
) : FlowUseCase<Unit, Categories>(dispatcher) {

    override fun execute(params: Unit): Flow<Categories> {
        return categoryRepository.getCategoriesFlow()
    }
}
