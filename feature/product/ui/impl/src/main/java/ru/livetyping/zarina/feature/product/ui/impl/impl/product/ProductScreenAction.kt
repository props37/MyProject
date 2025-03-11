package ru.livetyping.zarina.feature.product.ui.impl.impl.product

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer

internal sealed interface ProductScreenAction {
    data object BackClicked : ProductScreenAction

    data class CheckAvailabilityInStoresClicked(val product: Product) : ProductScreenAction

    data class SubscribeToProductClicked(
        val product: Product,
        val offer: ProductOffer,
    ) : ProductScreenAction

    data class ProductClicked(val product: Product) : ProductScreenAction
}
