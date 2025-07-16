package ru.livetyping.zarina.feature.onboarding.ui.impl.screen.model

internal sealed interface OnboardingEvent {
    data object RequestNotificationsPermissionClicked : OnboardingEvent

    data object DetectCityClicked : OnboardingEvent

    data object SkipCityDetectionClicked : OnboardingEvent

    data object ConfirmCityClicked : OnboardingEvent

    data object SelectCityClicked : OnboardingEvent
}
