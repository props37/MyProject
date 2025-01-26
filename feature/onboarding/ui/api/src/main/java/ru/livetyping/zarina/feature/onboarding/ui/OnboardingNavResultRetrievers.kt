package ru.livetyping.zarina.feature.onboarding.ui

import ru.livetyping.zarina.core.navigation.NavigationResultRetrievers
import ru.livetyping.zarina.core.navigationutil.ScreenResultRetriever

public class OnboardingNavResultRetrievers(
    public val selectedCityResultRetriever: ScreenResultRetriever<OnboardingSelectedCityResult>,
) : NavigationResultRetrievers
