package ru.zarina.zarina.ui.screen.products

import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.filter.Filters

sealed class ProductsScreenAction {
    data class FiltersClicked(
        val categoryId: Category.Id,
        val appliedFilters: Filters?,
    ) : ProductsScreenAction()
}
