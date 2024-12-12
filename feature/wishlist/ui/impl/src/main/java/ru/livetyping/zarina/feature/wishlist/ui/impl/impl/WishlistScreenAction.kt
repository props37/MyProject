package ru.livetyping.zarina.feature.wishlist.ui.impl.impl

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer

internal sealed interface WishlistScreenAction {
    data object BackClicked : WishlistScreenAction

    data object GoToCatalogClicked : WishlistScreenAction

    data class ProductClicked(val product: Product) : WishlistScreenAction

    data class SubscribeToProductClicked(
        val product: Product,
        val offer: ProductOffer,
    ) : WishlistScreenAction
}
