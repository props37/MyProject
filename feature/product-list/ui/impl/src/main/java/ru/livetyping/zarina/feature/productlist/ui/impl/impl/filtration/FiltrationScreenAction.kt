package ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration

import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilter
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters

internal sealed interface FiltrationScreenAction {
    data object BackClicked : FiltrationScreenAction

    data class FilterClicked(val filter: ProductFilter<*>) : FiltrationScreenAction

    data class ShowProductsClicked(val filters: ProductFilters) : FiltrationScreenAction
}
