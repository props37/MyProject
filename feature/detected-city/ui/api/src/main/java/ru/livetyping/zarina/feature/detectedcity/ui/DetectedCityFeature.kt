package ru.livetyping.zarina.feature.detectedcity.ui

import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import kotlin.reflect.KClass

// TODO: [Low] Migrate to DialogFeatureEntry
public interface DetectedCityFeature :
    ComposableFeatureEntry<DetectedCityNavEntry, DetectedCityNavActions, EmptyNavResultRetrievers> {

    public companion object {
        public fun getNavEntry(
            cityName: String = DetectedCityNavEntry.getDefaultCityName(),
        ): DetectedCityNavEntry {
            return DetectedCityNavEntry(cityName)
        }

        public fun getNavEntryClass(): KClass<DetectedCityNavEntry> = DetectedCityNavEntry::class
    }
}
