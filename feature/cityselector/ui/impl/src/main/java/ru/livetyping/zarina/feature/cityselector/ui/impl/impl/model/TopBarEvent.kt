package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model

internal sealed interface TopBarEvent {
    data object BackClicked : TopBarEvent
}
