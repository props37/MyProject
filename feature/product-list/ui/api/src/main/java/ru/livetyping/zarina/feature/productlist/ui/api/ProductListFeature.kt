package ru.livetyping.zarina.feature.productlist.ui.api

import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import kotlin.reflect.KClass

public interface ProductListFeature :
    ComposableFeatureEntry<ProductListNavEntry, ProductListNavActions, EmptyNavResultRetrievers> {

    public companion object {
        public fun getNavEntry(params: ProductListNavParams): ProductListNavEntry {
            return params.toNavEntry()
        }

        public fun getNavEntryClass(): KClass<ProductListNavEntry> = ProductListNavEntry::class
    }
}
