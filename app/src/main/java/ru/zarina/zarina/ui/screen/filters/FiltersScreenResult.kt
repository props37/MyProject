package ru.zarina.zarina.ui.screen.filters

import ru.zarina.zarina.domain.filter.Filters

sealed class FiltersScreenResult {
    data object ScreenClosed : FiltersScreenResult()

    data class FiltersChanged(val filters: Filters) : FiltersScreenResult()
}
