package ru.zarina.zarina.ui.screens.catalog.categories

import ru.zarina.zarina.usecase.catalog.GetCategoriesUseCase
import ru.zarina.zarina.utils.clean.invoke
import javax.inject.Inject

class CategoriesInteractor @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
) {
    suspend fun getCategories() = getCategoriesUseCase()
}
