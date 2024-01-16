package ru.zarina.zarina.ui.screen.cityselector

import ru.zarina.zarina.usecase.rework.geography.GetCitiesFlowUseCase
import javax.inject.Inject

class CitySelectorInteractor @Inject constructor(
    val getCitiesFlow: GetCitiesFlowUseCase
)
