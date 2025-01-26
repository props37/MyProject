package ru.livetyping.zarina.feature.product.ui.api

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.navigation.NavigationActions

public class ProductNavActions(
    public val onBackClicked: () -> Unit,
    public val onSubscribeToProductClicked: (Product, ProductOffer) -> Unit,
) : NavigationActions
