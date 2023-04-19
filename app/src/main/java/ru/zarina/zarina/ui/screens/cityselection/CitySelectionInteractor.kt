package ru.zarina.zarina.ui.screens.cityselection

import ru.zarina.zarina.usecase.geography.GetCititesUseCase
import javax.inject.Inject

class CitySelectionInteractor @Inject constructor(
    private val getCitiesUseCase: GetCititesUseCase,
) {
    suspend fun getCities(query: String?) =
        getCitiesUseCase(GetCititesUseCase.Params(query))
}
