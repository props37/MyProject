package ru.zarina.zarina.ui.screens.catalog.categories

import org.koin.core.annotation.Factory
import ru.zarina.zarina.usecase.catalog.FetchCategoriesUseCase
import ru.zarina.zarina.utils.clean.invoke

@Factory
class CategoriesInteractor(
    private val fetchCategoriesUseCase: FetchCategoriesUseCase,
) {
    suspend fun fetchCategories() = fetchCategoriesUseCase()
}
