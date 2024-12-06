package ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model

import ru.livetyping.zarina.core.domain.model.product.Product

internal sealed interface WishlistEvent {
    data class ProductClicked(val product: Product) : WishlistEvent

    data class AddToWishlistClicked(val product: Product) : WishlistEvent

    data class AddToCartClicked(val product: Product) : WishlistEvent

    data class SubscribeClicked(val product: Product) : WishlistEvent

    data object GoToCatalogClicked : WishlistEvent
}
