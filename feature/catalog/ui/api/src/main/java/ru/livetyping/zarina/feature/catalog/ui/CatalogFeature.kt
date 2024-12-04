package ru.livetyping.zarina.feature.catalog.ui

import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import kotlin.reflect.KClass

public interface CatalogFeature : ComposableFeatureEntry<CatalogNavEntry, CatalogNavActions> {
    public companion object {
        public fun getNavEntry(): CatalogNavEntry = CatalogNavEntry

        public fun getNavEntryClass(): KClass<CatalogNavEntry> = CatalogNavEntry::class
    }
}
