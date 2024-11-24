package ru.livetyping.zarina.feature.catalog.ui.impl.impl.model

internal sealed interface CategoryListEvent {
    data class ItemClicked(val item: CategoryListItem) : CategoryListEvent

    data object ErrorRefreshClicked : CategoryListEvent
}
