package ru.livetyping.zarina.feature.productlist.ui.api

import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters

public class ProductListNavActions(
    public val onBackClicked: () -> Unit,
    public val onTagClicked: (Category, ProductFilters?) -> Unit,
    public val onSubscribeToProductClicked: (Product, ProductOffer) -> Unit,
)
