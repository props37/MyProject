package ru.livetyping.zarina.presentation.screen.productsearch.filters

import ru.livetyping.zarina.domain.filter.ListFilter

sealed class ProductSearchFiltersScreenAction {
    data class ListFilterClicked(val filter: ListFilter<*>) : ProductSearchFiltersScreenAction()
}
