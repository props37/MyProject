package ru.livetyping.zarina.feature.wishlist.ui.impl.impl

internal sealed interface WishlistScreenAction {
    data object BackClicked : WishlistScreenAction

    data object GoToCatalogClicked : WishlistScreenAction
}
