package ru.livetyping.zarina.feature.search.ui.impl.impl.search.model

internal sealed interface SearchBarEvent {
    data object SearchClicked : SearchBarEvent

    data object Focused : SearchBarEvent

    data object CancelClicked : SearchBarEvent

    data object BackClicked : SearchBarEvent

    data object FiltersClicked : SearchBarEvent
}
