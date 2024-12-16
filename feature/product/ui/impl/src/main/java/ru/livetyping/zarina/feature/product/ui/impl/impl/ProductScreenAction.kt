package ru.livetyping.zarina.feature.product.ui.impl.impl

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer

internal sealed interface ProductScreenAction {
    data object BackClicked : ProductScreenAction

    data class SubscribeToProductClicked(
        val product: Product,
        val offer: ProductOffer,
    ) : ProductScreenAction
}
