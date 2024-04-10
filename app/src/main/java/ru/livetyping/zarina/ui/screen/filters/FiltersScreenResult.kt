package ru.livetyping.zarina.ui.screen.filters

import ru.livetyping.zarina.domain.filter.Filters

sealed class FiltersScreenResult {
    data object ScreenClosed : FiltersScreenResult()

    data class FiltersChanged(val filters: Filters) : FiltersScreenResult()
}
