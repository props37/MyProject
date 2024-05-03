package ru.livetyping.zarina.ui.screen.product

import ru.livetyping.zarina.usecase.product.GetProductFlowUseCase
import javax.inject.Inject

class ProductInteractor @Inject constructor(
    val getProductFlow: GetProductFlowUseCase,
)
