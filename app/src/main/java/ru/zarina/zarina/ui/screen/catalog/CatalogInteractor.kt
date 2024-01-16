package ru.zarina.zarina.ui.screen.catalog

import ru.zarina.zarina.usecase.rework.category.GetCategoriesFlowUseCase
import javax.inject.Inject

class CatalogInteractor @Inject constructor(
    val getCategoriesFlow: GetCategoriesFlowUseCase,
)
