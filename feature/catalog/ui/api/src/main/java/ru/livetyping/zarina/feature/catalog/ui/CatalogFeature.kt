package ru.livetyping.zarina.feature.catalog.ui

import ru.livetyping.zarina.core.feature.SingleFeatureEntry

public interface CatalogFeature : SingleFeatureEntry<CatalogNavEntry, Unit, CatalogNavActions> {
    public companion object {
        public val NavEntry: CatalogNavEntry = CatalogNavEntry
    }
}
