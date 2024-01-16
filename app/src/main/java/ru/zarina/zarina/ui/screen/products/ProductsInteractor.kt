package ru.zarina.zarina.ui.screen.products

import ru.zarina.zarina.usecase.rework.category.GetCategoryFlowUseCase
import javax.inject.Inject

class ProductsInteractor @Inject constructor(
    val getCategoryFlow: GetCategoryFlowUseCase,
)
