package ru.livetyping.zarina.feature.wishlist.ui

import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import kotlin.reflect.KClass

public interface WishlistFeature : ComposableFeatureEntry<WishlistNavEntry, WishlistNavActions> {
    public companion object {
        public fun getNavEntry(): WishlistNavEntry = WishlistNavEntry

        public fun getNavEntryClass(): KClass<WishlistNavEntry> = WishlistNavEntry::class
    }
}
