package ru.zarina.zarina.usecase.old.catalog

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.old.category.ICategoryRepository
import ru.zarina.zarina.di.old.Qualifiers
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.base.usecase.UseCase

@Factory
class FetchCategoriesUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val categoryRepository: ICategoryRepository,
) : UseCase<Unit, List<Category>>(dispatcher) {
    override suspend fun execute(params: Unit): List<Category> {
        return categoryRepository.fetchCategories()
    }
}
