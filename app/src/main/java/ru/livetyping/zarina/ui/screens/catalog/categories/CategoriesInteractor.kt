package ru.livetyping.zarina.ui.screens.catalog.categories

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.usecase.old.catalog.FetchCategoriesUseCase
import ru.livetyping.zarina.util.base.usecase.invoke

@Factory
class CategoriesInteractor(
    private val fetchCategoriesUseCase: FetchCategoriesUseCase,
) {
    suspend fun fetchCategories() = fetchCategoriesUseCase()
}
