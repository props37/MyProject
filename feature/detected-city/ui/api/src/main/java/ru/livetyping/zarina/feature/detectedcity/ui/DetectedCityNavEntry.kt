package ru.livetyping.zarina.feature.detectedcity.ui

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.navigation.NavigationEntry

@Serializable
public data class DetectedCityNavEntry(
    val cityName: String,
) : NavigationEntry
