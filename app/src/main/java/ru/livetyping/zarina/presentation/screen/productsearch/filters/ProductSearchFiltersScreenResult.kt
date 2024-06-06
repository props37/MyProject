package ru.livetyping.zarina.presentation.screen.productsearch.filters

import ru.livetyping.zarina.domain.filter.Filters

sealed class ProductSearchFiltersScreenResult {
    data object ScreenClosed : ProductSearchFiltersScreenResult()

    data class FiltersChanged(val filters: Filters) : ProductSearchFiltersScreenResult()
}
