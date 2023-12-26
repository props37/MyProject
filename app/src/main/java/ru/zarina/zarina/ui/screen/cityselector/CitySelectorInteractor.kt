package ru.zarina.zarina.ui.screen.cityselector

import ru.zarina.zarina.usecase.rework.geography.GetCitiesUseCase
import javax.inject.Inject

class CitySelectorInteractor @Inject constructor(
    val getCities: GetCitiesUseCase
)
