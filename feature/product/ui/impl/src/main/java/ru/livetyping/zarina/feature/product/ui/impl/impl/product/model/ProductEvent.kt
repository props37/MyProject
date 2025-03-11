package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductColor

internal sealed interface ProductEvent {
    data class ProductColorClicked(val color: ProductColor) : ProductEvent

    data object CheckAvailabilityInStoresClicked : ProductEvent

    data class AddToCartClicked(val product: Product) : ProductEvent

    data class AddToWishlistClicked(val product: Product) : ProductEvent

    data object ErrorRefreshClicked : ProductEvent
}
