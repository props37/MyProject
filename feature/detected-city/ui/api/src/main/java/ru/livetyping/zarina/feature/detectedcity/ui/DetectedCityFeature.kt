package ru.livetyping.zarina.feature.detectedcity.ui

import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import kotlin.reflect.KClass

public interface DetectedCityFeature :
    ComposableFeatureEntry<DetectedCityNavEntry, DetectedCityNavActions, Unit> {

    public companion object {
        public fun getNavEntryClass(): KClass<DetectedCityNavEntry> = DetectedCityNavEntry::class
    }
}
