package ru.livetyping.zarina.feature.onboarding.ui.impl.impl

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface OnboardingSideEffect : SideEffect {
    data class Navigate(val action: OnboardingScreenAction) : OnboardingSideEffect
}
