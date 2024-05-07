package ru.livetyping.zarina.presentation.screen.filters

import ru.livetyping.zarina.usecase.product.GetCategoryProductInfoFlowUseCase
import javax.inject.Inject

class FiltersInteractor @Inject constructor(
    val getCategoryProductInfoFlow: GetCategoryProductInfoFlowUseCase,
)
