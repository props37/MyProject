package ru.livetyping.zarina.feature.onboarding.ui

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigation.NavigationResultRetrievers
import ru.livetyping.zarina.core.navigationutil.ScreenResultRetriever
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature.NavActions
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature.NavEntry
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature.NavResultRetrievers

public interface OnboardingFeature :
    ComposableFeatureEntry<NavEntry, NavActions, NavResultRetrievers> {

    @Serializable
    public data object NavEntry : NavigationEntry

    public class NavActions(
        public val onOnboardingCompleted: (selectedCity: City?) -> Unit,
        public val onSelectCityClicked: () -> Unit,
    ) : NavigationActions

    public class NavResultRetrievers(
        public val selectedCityResultRetriever: ScreenResultRetriever<OnboardingSelectedCityResult>,
    ) : NavigationResultRetrievers
}
