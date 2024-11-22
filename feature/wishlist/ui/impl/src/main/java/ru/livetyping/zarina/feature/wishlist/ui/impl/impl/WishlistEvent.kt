package ru.livetyping.zarina.feature.wishlist.ui.impl.impl

internal sealed interface WishlistEvent {
    data object ClearWishlistClicked : WishlistEvent

    data object GoToCatalogClicked : WishlistEvent
}
