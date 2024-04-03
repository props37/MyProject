package ru.livetyping.zarina.usecase.old.catalog

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.old.category.ICategoryRepository
import ru.livetyping.zarina.di.old.Qualifiers
import ru.livetyping.zarina.domain.old.Category

@Factory
class FetchCategoriesUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val categoryRepository: ICategoryRepository,
) : UseCase<Unit, List<Category>>(dispatcher) {
    override suspend fun execute(params: Unit): List<Category> {
        return categoryRepository.fetchCategories()
    }
}
