package ru.zarina.zarina.ui.screen.filters

import ru.zarina.zarina.usecase.rework.product.GetCategoryProductInfoFlowUseCase
import javax.inject.Inject

class FiltersInteractor @Inject constructor(
    val getCategoryProductInfoFlow: GetCategoryProductInfoFlowUseCase,
)
