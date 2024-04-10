package ru.livetyping.zarina.ui.screen.cityselector

import ru.livetyping.zarina.usecase.geography.GetCitiesFlowUseCase
import javax.inject.Inject

class CitySelectorInteractor @Inject constructor(
    val getCitiesFlow: GetCitiesFlowUseCase,
)
