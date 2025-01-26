package ru.livetyping.zarina.feature.catalog.ui

import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationEntry
import kotlin.reflect.KClass

public interface CatalogFeature :
    ComplexFeatureEntry<CatalogNavEntry, CatalogNavActions, EmptyNavResultRetrievers> {

    public companion object {
        public fun getNavEntry(): CatalogNavEntry = CatalogNavEntry

        public fun getNavEntryClass(): KClass<CatalogNavEntry> = CatalogNavEntry::class

        public fun getStartNavEntry(): NavigationEntry = CatalogNavEntry.StartNavEntry
    }
}
