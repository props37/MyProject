package ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model

internal sealed interface TopBarEvent {
    data object ClearWishlistClicked : TopBarEvent
}
