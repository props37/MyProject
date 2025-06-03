package ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model

import ru.livetyping.zarina.core.domain.model.product.Product

internal sealed interface WishlistEvent {
    data object BackClicked : WishlistEvent

    data class ProductClicked(val product: Product) : WishlistEvent

    data class AddProductToWishlistClicked(val product: Product) : WishlistEvent

    data class ProductAppendError(val throwable: Throwable) : WishlistEvent
}
