package ru.livetyping.zarina.feature.product.ui.api

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer

public class ProductNavActions(
    public val onBackClicked: () -> Unit,
    public val onSubscribeToProductClicked: (Product, ProductOffer) -> Unit,
)
