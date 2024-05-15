package ru.livetyping.zarina.presentation.screen.catalog

import ru.livetyping.zarina.domain.category.Category

sealed class CatalogScreenAction {
    data class CategoryClicked(val categoryId: Category.Id) : CatalogScreenAction()
}
