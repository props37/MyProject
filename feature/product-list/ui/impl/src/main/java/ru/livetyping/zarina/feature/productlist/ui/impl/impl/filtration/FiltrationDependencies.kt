package ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration

import ru.livetyping.zarina.core.domain.usecase.product.GetCategoryInfoFlowUseCase
import javax.inject.Inject

internal class FiltrationDependencies @Inject constructor(
    val getCategoryInfoFlow: GetCategoryInfoFlowUseCase,
)
