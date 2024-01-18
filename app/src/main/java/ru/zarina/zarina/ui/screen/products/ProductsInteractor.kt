package ru.zarina.zarina.ui.screen.products

import ru.zarina.zarina.usecase.rework.category.GetCategoryFlowUseCase
import ru.zarina.zarina.usecase.rework.product.GetProductPagingDataFlowUseCase
import javax.inject.Inject

class ProductsInteractor @Inject constructor(
    val getCategoryFlow: GetCategoryFlowUseCase,
    val getProductPagingDataFlow: GetProductPagingDataFlowUseCase,
)
