package ru.livetyping.zarina.feature.product.ui.impl.impl.product

import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.domain.usecase.product.GetProductUseCase
import javax.inject.Inject

internal class ProductDependencies @Inject constructor(
    val getProduct: GetProductUseCase,
    val appMetrica: AppMetrica,
)
