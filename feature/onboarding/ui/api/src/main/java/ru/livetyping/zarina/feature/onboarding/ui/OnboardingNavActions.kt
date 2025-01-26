package ru.livetyping.zarina.feature.onboarding.ui

import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.navigation.NavigationActions

public class OnboardingNavActions(
    public val onOnboardingCompleted: (selectedCity: City?) -> Unit,
    public val onSelectCityClicked: () -> Unit,
) : NavigationActions
