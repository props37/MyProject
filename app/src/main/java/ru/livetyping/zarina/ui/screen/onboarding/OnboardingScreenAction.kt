package ru.livetyping.zarina.ui.screen.onboarding

import ru.livetyping.zarina.domain.geography.City

sealed class OnboardingScreenAction {
    data class OnboardingCompleted(val userCity: City?) : OnboardingScreenAction()

    data class SelectCityClicked(val currentCity: City?) : OnboardingScreenAction()
}
