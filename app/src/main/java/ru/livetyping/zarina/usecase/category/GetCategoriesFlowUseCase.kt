package ru.livetyping.zarina.usecase.category

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.category.CategoryRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.category.Categories
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
