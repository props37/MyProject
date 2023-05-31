package ru.zarina.zarina.usecase.catalog

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.category.ICategoryRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.Category
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val categoryRepository: ICategoryRepository,
) : UseCase<Unit, List<Category>>(dispatcher) {
    override suspend fun execute(params: Unit): List<Category> {
        return categoryRepository.getCategories()
    }
}
