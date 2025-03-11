package ru.livetyping.zarina.feature.search.ui.impl.impl.filtration

import ru.livetyping.zarina.core.domain.usecase.search.SearchFlowUseCase
import javax.inject.Inject

internal class FiltrationDependencies @Inject constructor(
    val search: SearchFlowUseCase,
)
