package ru.zarina.zarina.ui.screens

import ru.zarina.zarina.usecase.location.DetectCityUseCase
import ru.zarina.zarina.utils.clean.invoke
import javax.inject.Inject

class OnboardingInteractor @Inject constructor(
    private val detectCityUseCase: DetectCityUseCase,
) {
    suspend fun detectCity() = detectCityUseCase()
}
