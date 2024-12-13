package ru.livetyping.zarina.feature.product.ui.impl.impl

import ru.livetyping.zarina.core.domain.usecase.product.GetProductFlowUseCase
import javax.inject.Inject

internal class ProductDeps @Inject constructor(
    val getProductFlow: GetProductFlowUseCase,
)
