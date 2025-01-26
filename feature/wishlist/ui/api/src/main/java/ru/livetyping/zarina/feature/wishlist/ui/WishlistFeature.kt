package ru.livetyping.zarina.feature.wishlist.ui

import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationEntry
import kotlin.reflect.KClass

public interface WishlistFeature :
    ComplexFeatureEntry<WishlistNavEntry, WishlistNavActions, EmptyNavResultRetrievers> {

    public companion object {
        public fun getNavEntry(): WishlistNavEntry = WishlistNavEntry

        public fun getNavEntryClass(): KClass<WishlistNavEntry> = WishlistNavEntry::class

        public fun getStartNavEntry(): NavigationEntry = WishlistNavEntry.StartNavEntry
    }
}
