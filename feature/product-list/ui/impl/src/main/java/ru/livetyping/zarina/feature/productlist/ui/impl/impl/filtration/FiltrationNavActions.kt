package ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration

import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters

internal class FiltrationNavActions(
    val onBackClicked: () -> Unit,
    val onShowProductsClicked: (ProductFilters) -> Unit,
)
