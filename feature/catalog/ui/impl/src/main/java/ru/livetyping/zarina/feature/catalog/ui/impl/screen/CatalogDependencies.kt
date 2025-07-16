package ru.livetyping.zarina.feature.catalog.ui.impl.screen

import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.domain.usecase.catalog.GetCatalogMenuUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.feedback.Feedback
import javax.inject.Inject

internal class CatalogDependencies @Inject constructor(
    val getCatalogMenu: GetCatalogMenuUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
    val appMetrica: AppMetrica,
    val feedback: Feedback,
)
