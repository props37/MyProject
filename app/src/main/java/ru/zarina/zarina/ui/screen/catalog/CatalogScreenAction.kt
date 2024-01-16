package ru.zarina.zarina.ui.screen.catalog

import ru.zarina.zarina.domain.rework.common.Category

sealed class CatalogScreenAction {
    data class CategoryClicked(val category: Category) : CatalogScreenAction()
}
