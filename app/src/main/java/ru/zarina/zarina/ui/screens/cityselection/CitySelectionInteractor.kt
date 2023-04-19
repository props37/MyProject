package ru.zarina.zarina.ui.screens.cityselection

import ru.zarina.zarina.usecase.geography.GetCitiesUseCase
import javax.inject.Inject

class CitySelectionInteractor @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
) {
    suspend fun getCities(query: String?) =
        getCitiesUseCase(GetCitiesUseCase.Params(query))
}
