package ru.livetyping.zarina.feature.productlist.ui.api

import ru.livetyping.zarina.core.feature.SingleFeatureEntry
import kotlin.reflect.KClass

public interface ProductListFeature : SingleFeatureEntry<ProductListNavEntry, ProductListNavParams, ProductListNavActions> {

    public companion object {
        public fun getNavEntry(params: ProductListNavParams): ProductListNavEntry {
            return params.toNavEntry()
        }

        public fun getNavEntryClass(): KClass<ProductListNavEntry> = ProductListNavEntry::class
    }
}
