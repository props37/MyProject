package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.model

import ru.livetyping.zarina.core.domain.model.product.Product

internal sealed interface ProductEvent {
    data class ProductClicked(val product: Product) : ProductEvent

    data class AddToWishlistClicked(val product: Product) : ProductEvent

    data class AddToCartClicked(val product: Product) : ProductEvent

    data class SubscribeClicked(val product: Product) : ProductEvent

    data object ProductsRefreshed : ProductEvent

    data object ProductsErrorRefreshClicked : ProductEvent
}
