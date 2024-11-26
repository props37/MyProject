package ru.livetyping.zarina.feature.cityselector.ui

import ru.livetyping.zarina.core.feature.SingleFeatureEntry
import kotlin.reflect.KClass

public interface CitySelectorFeature :
    SingleFeatureEntry<CitySelectorNavEntry, CitySelectorNavParams, CitySelectorNavActions> {

    public companion object {
        public fun getNavEntry(params: CitySelectorNavParams): CitySelectorNavEntry {
            return params.toNavEntry()
        }

        public fun getNavEntryClass(): KClass<CitySelectorNavEntry> = CitySelectorNavEntry::class
    }
}
