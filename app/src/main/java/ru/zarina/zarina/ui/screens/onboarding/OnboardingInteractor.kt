package ru.zarina.zarina.ui.screens.onboarding

import ru.zarina.zarina.usecase.content.GetOnboardingSplashUseCase
import ru.zarina.zarina.usecase.location.DetectCityUseCase
import ru.zarina.zarina.utils.clean.invoke
import javax.inject.Inject

class OnboardingInteractor @Inject constructor(
    private val getOnboardingSpash: GetOnboardingSplashUseCase,
    private val detectCityUseCase: DetectCityUseCase,
) {
    suspend fun getOnboardingSplash() = getOnboardingSpash()
    suspend fun detectCity() = detectCityUseCase()
}
