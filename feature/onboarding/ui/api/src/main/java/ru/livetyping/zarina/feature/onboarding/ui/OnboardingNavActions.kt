package ru.livetyping.zarina.feature.onboarding.ui

import ru.livetyping.zarina.core.navigation.NavigationActions

public class OnboardingNavActions(
    public val onboardingCompleted: () -> Unit,
) : NavigationActions
