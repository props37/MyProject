package ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.model

internal sealed interface TopBarEvent {
    data object BackClicked : TopBarEvent

    data object ResetFiltersClicked : TopBarEvent
}
