package ru.livetyping.zarina.feature.wishlist.ui

import ru.livetyping.zarina.core.feature.SingleFeatureEntry
import kotlin.reflect.KClass

public interface WishlistFeature : SingleFeatureEntry<WishlistNavEntry, Unit, WishlistNavActions> {
    public companion object {
        public fun getNavEntry(): WishlistNavEntry = WishlistNavEntry

        public fun getNavEntryClass(): KClass<WishlistNavEntry> = WishlistNavEntry::class
    }
}
