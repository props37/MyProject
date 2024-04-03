package ru.livetyping.zarina.ui.screen.filters

import ru.livetyping.zarina.domain.filter.ListFilter

sealed class FiltersScreenAction {
    data class ListFilterClicked(val filter: ListFilter<*>) : FiltersScreenAction()
}
