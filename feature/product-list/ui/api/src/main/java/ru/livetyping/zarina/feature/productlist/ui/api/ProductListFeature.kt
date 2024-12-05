package ru.livetyping.zarina.feature.productlist.ui.api

import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import kotlin.reflect.KClass

public interface ProductListFeature :
    ComposableFeatureEntry<ProductListNavEntry, ProductListNavActions, Unit> {

    public companion object {
        public fun getNavEntry(params: ProductListNavParams): ProductListNavEntry {
            return params.toNavEntry()
        }

        public fun getNavEntryClass(): KClass<ProductListNavEntry> = ProductListNavEntry::class
    }
}
