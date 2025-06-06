package ru.livetyping.zarina.feature.product.ui.impl.impl.product

import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.domain.usecase.product.GetProductTotalLookUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetProductUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetSimilarProductsUseCase
import javax.inject.Inject

internal class ProductDependencies @Inject constructor(
    val getProduct: GetProductUseCase,
    val getProductTotalLook: GetProductTotalLookUseCase,
    val getSimilarProducts: GetSimilarProductsUseCase,
    val appMetrica: AppMetrica,
)
