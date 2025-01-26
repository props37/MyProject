package ru.livetyping.zarina.feature.home.ui

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.feature.home.domain.model.Banner
import ru.livetyping.zarina.feature.home.ui.HomeFeature.NavActions
import ru.livetyping.zarina.feature.home.ui.HomeFeature.NavEntry

public interface HomeFeature :
    ComplexFeatureEntry<NavEntry, NavActions, EmptyNavResultRetrievers> {

    @Serializable
    public data object NavEntry : NavigationEntry {

        @Serializable
        public data object StartNavEntry : NavigationEntry
    }

    public class NavActions(
        public val onBannerClicked: (Banner) -> Unit,
    ) : NavigationActions
}
