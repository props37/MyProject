package ru.livetyping.zarina.feature.product.ui.api

import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import kotlin.reflect.KClass

public interface ProductFeature : ComposableFeatureEntry<ProductNavEntry, ProductNavActions, Unit> {
    public companion object {
        public fun getNavEntry(params: ProductNavParams): ProductNavEntry {
            return params.toNavEntry()
        }

        public fun getNavEntryClass(): KClass<ProductNavEntry> = ProductNavEntry::class
    }
}
