package ru.zarina.zarina.ui.screen.onboarding

import ru.zarina.zarina.domain.geography.City

sealed class OnboardingScreenAction {
    data class OnboardingCompleted(val userCity: City?) : OnboardingScreenAction()

    data class SelectCityClicked(val currentCity: City?) : OnboardingScreenAction()
}
