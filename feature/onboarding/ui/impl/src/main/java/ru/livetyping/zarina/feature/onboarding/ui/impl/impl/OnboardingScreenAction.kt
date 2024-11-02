package ru.livetyping.zarina.feature.onboarding.ui.impl.impl

import ru.livetyping.zarina.core.domain.model.geo.City

internal sealed interface OnboardingScreenAction {
    data class OnboardingCompleted(val selectedCity: City?) : OnboardingScreenAction
}
