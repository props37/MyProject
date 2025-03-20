package ru.livetyping.zarina.feature.productlist.ui.impl.impl.listfilter

import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilter
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilterItem
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class ListFilterNavActions(
    val onBackClicked: () -> Unit,
    val onApplyClicked: (ProductListFilter<ProductListFilterItem>) -> Unit,
) : NavigationActions
