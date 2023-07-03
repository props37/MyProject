package ru.zarina.zarina.ui.screens.onboarding

import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.usecase.content.GetOnboardingSplashUseCase
import ru.zarina.zarina.usecase.location.DetectCityUseCase
import ru.zarina.zarina.usecase.onboarding.FinishOnboardingUseCase
import ru.zarina.zarina.utils.clean.invoke

@Factory
class OnboardingInteractor(
    private val getOnboardingSplashUseCase: GetOnboardingSplashUseCase,
    private val detectCityUseCase: DetectCityUseCase,
    private val finishOnboardingUseCase: FinishOnboardingUseCase,
) {
    suspend fun getOnboardingSplash() = getOnboardingSplashUseCase()
    suspend fun detectCity() = detectCityUseCase()
    suspend fun finishOnboarding(selectedCity: City?) = finishOnboardingUseCase(
        FinishOnboardingUseCase.Params(selectedCity)
    )
}
