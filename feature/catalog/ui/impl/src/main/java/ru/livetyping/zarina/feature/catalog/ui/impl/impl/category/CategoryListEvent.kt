package ru.livetyping.zarina.feature.catalog.ui.impl.impl.category

internal sealed interface CategoryListEvent {
    data class ItemClicked(val item: CategoryListItem) : CategoryListEvent

    data object ErrorRefreshClicked : CategoryListEvent
}
