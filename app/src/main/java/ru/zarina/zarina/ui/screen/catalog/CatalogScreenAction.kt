package ru.zarina.zarina.ui.screen.catalog

import ru.zarina.zarina.domain.category.Category

sealed class CatalogScreenAction {
    data class CategoryClicked(val categoryId: Category.Id) : CatalogScreenAction()
}
