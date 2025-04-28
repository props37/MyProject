package ru.livetyping.zarina.feature.catalog.ui.impl.impl

import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoriesFlowUseCase
import javax.inject.Inject

internal class CatalogDependencies @Inject constructor(
    val getCategoriesFlow: GetCategoriesFlowUseCase,
    val appMetrica: AppMetrica,
)
