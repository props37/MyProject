package ru.livetyping.zarina.feature.wishlist.ui

import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import kotlin.reflect.KClass

public interface WishlistFeature : ComplexFeatureEntry<WishlistNavEntry, WishlistNavActions, Unit> {
    public companion object {
        public fun getNavEntry(): WishlistNavEntry = WishlistNavEntry

        public fun getNavEntryClass(): KClass<WishlistNavEntry> = WishlistNavEntry::class
    }
}
