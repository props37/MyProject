package ru.livetyping.zarina.ui.screen.catalog

import ru.livetyping.zarina.domain.category.Category

sealed class CatalogScreenAction {
    data class CategoryClicked(val categoryId: Category.Id) : CatalogScreenAction()
}
