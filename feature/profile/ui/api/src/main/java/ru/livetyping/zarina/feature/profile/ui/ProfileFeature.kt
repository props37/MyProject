package ru.livetyping.zarina.feature.profile.ui

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.feature.profile.ui.ProfileFeature.NavActions
import ru.livetyping.zarina.feature.profile.ui.ProfileFeature.NavEntry

public interface ProfileFeature :
    ComplexFeatureEntry<NavEntry, NavActions, ProfileNavResultRetrievers> {

    @Serializable
    public object NavEntry : NavigationEntry {

        @Serializable
        public data object StartNavEntry : NavigationEntry
    }

    public class NavActions(
        public val onBackClicked: () -> Unit,
        public val onSignInClicked: () -> Unit,
        public val onSignUpClicked: () -> Unit,
        public val onChangeCityClicked: (currentCity: City?) -> Unit,
    ) : NavigationActions
}
