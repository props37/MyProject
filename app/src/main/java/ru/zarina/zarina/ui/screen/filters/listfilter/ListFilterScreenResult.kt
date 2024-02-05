package ru.zarina.zarina.ui.screen.filters.listfilter

import ru.zarina.zarina.domain.rework.filter.ListFilter
import ru.zarina.zarina.domain.rework.filter.ListFilterItem

sealed class ListFilterScreenResult {
    data object ScreenClosed : ListFilterScreenResult()

    data class FilterChanged(val filter: ListFilter<ListFilterItem>) : ListFilterScreenResult()
}
