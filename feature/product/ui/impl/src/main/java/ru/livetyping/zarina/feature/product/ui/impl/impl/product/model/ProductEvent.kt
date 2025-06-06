package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

import ru.livetyping.zarina.core.domain.model.product.Product

internal sealed interface ProductEvent {
    data object BackClicked : ProductEvent

    data object ShareClicked : ProductEvent

    data class ProductClicked(val product: Product) : ProductEvent

    data class AddProductToWishlistClicked(val product: Product) : ProductEvent

    data object ProductRefreshTriggered : ProductEvent

    data object TotalLookProductRefreshTriggered : ProductEvent

    data object SimilarProductRefreshTriggered : ProductEvent
}
