package ru.livetyping.zarina.feature.productlist.ui.impl.impl.model

internal sealed interface TopBarEvent {
    data object BackClicked : TopBarEvent

    data object SearchClicked : TopBarEvent

    data object FiltersClicked : TopBarEvent
}
