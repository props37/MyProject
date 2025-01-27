package ru.livetyping.zarina.feature.detectedcity.ui

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.feature.detectedcity.ui.DetectedCityFeature.NavActions
import ru.livetyping.zarina.feature.detectedcity.ui.DetectedCityFeature.NavEntry

// TODO: [Low] Migrate to DialogFeatureEntry
public interface DetectedCityFeature :
    ComposableFeatureEntry<NavEntry, NavActions, EmptyNavResultRetrievers> {

    @Serializable
    public data class NavEntry(
        val cityName: String = getDefaultCityName(),
    ) : NavigationEntry {
        internal companion object {
            fun getDefaultCityName(): String = City.getDefault().name
        }
    }

    public class NavActions(
        public val onCloseClicked: () -> Unit,
    ) : NavigationActions
}
