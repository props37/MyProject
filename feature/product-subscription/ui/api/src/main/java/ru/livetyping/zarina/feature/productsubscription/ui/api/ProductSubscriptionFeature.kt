package ru.livetyping.zarina.feature.productsubscription.ui.api

import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import kotlin.reflect.KClass

public interface ProductSubscriptionFeature :
    ComposableFeatureEntry<ProductSubscriptionNavEntry, ProductSubscriptionNavActions, EmptyNavResultRetrievers> {

    public companion object {
        public fun getNavEntry(params: ProductSubscriptionNavParams): ProductSubscriptionNavEntry {
            return params.toNavEntry()
        }

        public fun getNavEntryClass(): KClass<ProductSubscriptionNavEntry> =
            ProductSubscriptionNavEntry::class
    }
}
