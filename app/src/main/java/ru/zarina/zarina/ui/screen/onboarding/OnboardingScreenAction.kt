package ru.zarina.zarina.ui.screen.onboarding

import ru.zarina.zarina.domain.rework.geography.City

sealed class OnboardingScreenAction {
    data class OnboardingCompleted(val currentCity: City?) : OnboardingScreenAction()

    data class SelectCityClicked(val currentCity: City?) : OnboardingScreenAction()
}
