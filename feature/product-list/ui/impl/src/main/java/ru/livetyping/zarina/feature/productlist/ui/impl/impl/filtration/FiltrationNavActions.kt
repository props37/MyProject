package ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration

import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilter
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class FiltrationNavActions(
    val onBackClicked: () -> Unit,
    val onFilterClicked: (ProductFilter<*>) -> Unit,
    val onShowProductsClicked: (ProductFilters) -> Unit,
) : NavigationActions
