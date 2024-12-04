package ru.livetyping.zarina.feature.catalog.ui.impl.impl

import ru.livetyping.zarina.core.domain.model.category.Category

internal sealed interface CatalogScreenAction {
    data object BackClicked : CatalogScreenAction

    data object SearchClicked : CatalogScreenAction

    data class CategoryClicked(val categoryId: Category.Id) : CatalogScreenAction
}
