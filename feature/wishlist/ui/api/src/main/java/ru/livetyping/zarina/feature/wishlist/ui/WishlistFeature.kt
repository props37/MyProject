package ru.livetyping.zarina.feature.wishlist.ui

import ru.livetyping.zarina.core.feature.SingleFeatureEntry

public interface WishlistFeature : SingleFeatureEntry<WishlistNavEntry, Unit, WishlistNavActions> {
    public companion object {
        public val NavEntry: WishlistNavEntry = WishlistNavEntry
    }
}
