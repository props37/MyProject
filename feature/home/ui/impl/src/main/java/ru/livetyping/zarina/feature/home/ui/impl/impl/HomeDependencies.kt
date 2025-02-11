package ru.livetyping.zarina.feature.home.ui.impl.impl

import ru.livetyping.zarina.core.domain.usecase.gender.GetLastContentGenderFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.gender.SetLastContentGenderUseCase
import ru.livetyping.zarina.feature.home.domain.usecase.GetHomeContentFlowUseCase
import javax.inject.Inject

internal class HomeDependencies @Inject constructor(
    val getLastContentGenderFlow: GetLastContentGenderFlowUseCase,
    val setLastContentGender: SetLastContentGenderUseCase,
    val getHomeContentFlow: GetHomeContentFlowUseCase,
)
