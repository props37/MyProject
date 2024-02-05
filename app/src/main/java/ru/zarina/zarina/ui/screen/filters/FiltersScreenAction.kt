package ru.zarina.zarina.ui.screen.filters

import ru.zarina.zarina.domain.rework.filter.ListFilter

sealed class FiltersScreenAction {
    data class ListFilterClicked(val filter: ListFilter<*>) : FiltersScreenAction()
}
