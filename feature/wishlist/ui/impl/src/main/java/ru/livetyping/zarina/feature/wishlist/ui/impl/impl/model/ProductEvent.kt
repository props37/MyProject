package ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model

import ru.livetyping.zarina.core.domain.model.product.Product

internal sealed interface ProductEvent {
    data class ProductClicked(val product: Product) : ProductEvent

    data class AddToFavoritesClicked(val product: Product) : ProductEvent

    data class AddToCartClicked(val product: Product) : ProductEvent

    data class SubscribeClicked(val product: Product) : ProductEvent
}
