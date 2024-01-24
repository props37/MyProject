package ru.zarina.zarina.ui.screen.catalog

import ru.zarina.zarina.domain.rework.category.Category

sealed class CatalogScreenAction {
    // TODO: [Low] Use Category.Id
    data class CategoryClicked(val category: Category) : CatalogScreenAction()
}
