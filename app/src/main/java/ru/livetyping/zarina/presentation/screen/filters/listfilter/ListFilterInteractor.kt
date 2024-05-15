package ru.livetyping.zarina.presentation.screen.filters.listfilter

import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import javax.inject.Inject

class ListFilterInteractor @Inject constructor(
    val getUserCityFlow: GetUserCityFlowUseCase,
)
