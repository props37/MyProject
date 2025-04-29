package ru.livetyping.zarina.feature.catalog.ui.impl.impl

import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.domain.usecase.catalog.GetCatalogMenuUseCase
import javax.inject.Inject

internal class CatalogDependencies @Inject constructor(
    val getCatalogMenu: GetCatalogMenuUseCase,
    val appMetrica: AppMetrica,
)
