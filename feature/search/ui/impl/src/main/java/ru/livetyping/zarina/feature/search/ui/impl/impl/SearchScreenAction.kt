package ru.livetyping.zarina.feature.search.ui.impl.impl

internal sealed interface SearchScreenAction {
    data object BackClicked : SearchScreenAction
}
