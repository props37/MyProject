package ru.livetyping.zarina.feature.cityselector.ui

import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import kotlin.reflect.KClass

public interface CitySelectorFeature :
    ComposableFeatureEntry<CitySelectorNavEntry, CitySelectorNavActions> {

    public companion object {
        public fun getNavEntry(params: CitySelectorNavParams): CitySelectorNavEntry {
            return params.toNavEntry()
        }

        public fun getNavEntryClass(): KClass<CitySelectorNavEntry> = CitySelectorNavEntry::class
    }
}
