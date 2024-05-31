package ru.livetyping.zarina.presentation.screen.products.filters.listfilter

import ru.livetyping.zarina.domain.filter.ListFilter
import ru.livetyping.zarina.domain.filter.ListFilterItem

sealed class ListFilterScreenResult {
    data object ScreenClosed : ListFilterScreenResult()

    data class FilterChanged(val filter: ListFilter<ListFilterItem>) : ListFilterScreenResult()
}
