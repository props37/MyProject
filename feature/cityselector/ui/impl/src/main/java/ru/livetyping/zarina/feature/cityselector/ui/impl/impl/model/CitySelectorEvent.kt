package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model

internal sealed interface CitySelectorEvent {
    data object BackClicked : CitySelectorEvent
}
