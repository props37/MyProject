package ru.livetyping.zarina.feature.wishlist.ui

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer

public class WishlistNavActions(
    public val onBackClicked: () -> Unit,
    public val onGoToCatalogClicked: () -> Unit,
    public val onProductClicked: (Product) -> Unit,
    public val onSubscribeToProductClicked: (Product, ProductOffer) -> Unit,
)
