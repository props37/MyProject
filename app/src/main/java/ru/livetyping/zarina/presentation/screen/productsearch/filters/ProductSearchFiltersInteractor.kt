package ru.livetyping.zarina.presentation.screen.productsearch.filters

import ru.livetyping.zarina.usecase.productsearch.SearchProductsFlowUseCase
import javax.inject.Inject

class ProductSearchFiltersInteractor @Inject constructor(
    val searchProductsFlow: SearchProductsFlowUseCase,
)
