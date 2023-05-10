package ru.zarina.zarina.ui.screens.selectcity

import ru.zarina.zarina.domain.City
import ru.zarina.zarina.usecase.geography.GetCitiesUseCase
import ru.zarina.zarina.usecase.onboarding.FinishOnboardingUseCase
import javax.inject.Inject

class SelectCityInteractor @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val finishOnboardingUseCase: FinishOnboardingUseCase,
) {
    suspend fun getCities(query: String?) =
        getCitiesUseCase(GetCitiesUseCase.Params(query))

    suspend fun finishOnboarding(city: City?) =
        finishOnboardingUseCase(FinishOnboardingUseCase.Params(city))
}
