package ru.livetyping.zarina.presentation.screen.products.filters

import ru.livetyping.zarina.usecase.product.GetCategoryProductInfoFlowUseCase
import javax.inject.Inject

class ProductFiltersInteractor @Inject constructor(
    val getCategoryProductInfoFlow: GetCategoryProductInfoFlowUseCase,
)
