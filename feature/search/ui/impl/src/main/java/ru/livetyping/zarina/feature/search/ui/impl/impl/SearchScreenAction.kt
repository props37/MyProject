package ru.livetyping.zarina.feature.search.ui.impl.impl

import ru.livetyping.zarina.core.domain.model.category.Category

internal sealed interface SearchScreenAction {
    data object BackClicked : SearchScreenAction

    data class CategoryClicked(val categoryId: Category.Id) : SearchScreenAction
}
