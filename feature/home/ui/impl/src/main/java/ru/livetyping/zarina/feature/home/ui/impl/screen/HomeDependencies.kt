package ru.livetyping.zarina.feature.home.ui.impl.screen

import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.feature.home.domain.usecase.GetHomeContentUseCase
import javax.inject.Inject

internal class HomeDependencies @Inject constructor(
    val getHomeContent: GetHomeContentUseCase,
    val appMetrica: AppMetrica,
)
