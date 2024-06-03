package ru.livetyping.zarina.presentation.screen.products.filters

import ru.livetyping.zarina.domain.filter.ListFilter

sealed class ProductFiltersScreenAction {
    data class ListFilterClicked(val filter: ListFilter<*>) : ProductFiltersScreenAction()
}
