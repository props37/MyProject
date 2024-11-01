package ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model

internal sealed interface OnboardingEvent {
    data object RequestNotificationsPermissionClicked : OnboardingEvent
}
