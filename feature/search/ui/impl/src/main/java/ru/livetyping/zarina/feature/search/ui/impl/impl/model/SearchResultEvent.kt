package ru.livetyping.zarina.feature.search.ui.impl.impl.model

import ru.livetyping.zarina.core.domain.model.product.Product

internal sealed interface SearchResultEvent {
    data class ProductClicked(val product: Product) : SearchResultEvent

    data class AddToWishlistClicked(val product: Product) : SearchResultEvent

    data class AddToCartClicked(val product: Product) : SearchResultEvent

    data class SubscribeClicked(val product: Product) : SearchResultEvent
}
