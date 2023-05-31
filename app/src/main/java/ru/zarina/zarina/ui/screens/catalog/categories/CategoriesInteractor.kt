package ru.zarina.zarina.ui.screens.catalog.categories

import ru.zarina.zarina.usecase.catalog.FetchCategoriesUseCase
import ru.zarina.zarina.utils.clean.invoke
import javax.inject.Inject

class CategoriesInteractor @Inject constructor(
    private val fetchCategoriesUseCase: FetchCategoriesUseCase,
) {
    suspend fun fetchCategories() = fetchCategoriesUseCase()
}
