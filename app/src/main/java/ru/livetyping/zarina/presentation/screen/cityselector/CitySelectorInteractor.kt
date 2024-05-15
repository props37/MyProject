package ru.livetyping.zarina.presentation.screen.cityselector

import ru.livetyping.zarina.usecase.geography.GetCitiesFlowUseCase
import javax.inject.Inject

class CitySelectorInteractor @Inject constructor(
    val getCitiesFlow: GetCitiesFlowUseCase,
)
