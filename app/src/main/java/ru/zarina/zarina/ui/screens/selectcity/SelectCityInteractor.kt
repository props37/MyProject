package ru.zarina.zarina.ui.screens.selectcity

import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.usecase.geography.GetCitiesUseCase
import ru.zarina.zarina.usecase.onboarding.FinishOnboardingUseCase

@Factory
class SelectCityInteractor(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val finishOnboardingUseCase: FinishOnboardingUseCase,
) {
    suspend fun getCities(query: String?) =
        getCitiesUseCase(GetCitiesUseCase.Params(query))

    suspend fun finishOnboarding(city: City?) =
        finishOnboardingUseCase(FinishOnboardingUseCase.Params(city))
}
