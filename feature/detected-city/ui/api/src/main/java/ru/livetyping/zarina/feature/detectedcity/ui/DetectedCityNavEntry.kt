package ru.livetyping.zarina.feature.detectedcity.ui

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.navigation.NavigationEntry

@Serializable
public data class DetectedCityNavEntry(
    val cityName: String = getDefaultCityName(),
) : NavigationEntry {
    internal companion object {
        fun getDefaultCityName(): String = City.getDefault().name
    }
}
