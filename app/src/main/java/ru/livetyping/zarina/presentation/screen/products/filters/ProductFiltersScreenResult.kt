package ru.livetyping.zarina.presentation.screen.products.filters

import ru.livetyping.zarina.domain.filter.Filters

sealed class ProductFiltersScreenResult {
    data object ScreenClosed : ProductFiltersScreenResult()

    data class FiltersChanged(val filters: Filters) : ProductFiltersScreenResult()
}
