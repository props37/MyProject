package ru.livetyping.zarina.feature.profile.ui.impl.impl.model

internal sealed interface ProfileEvent {
    data object ProfileDetailsClicked : ProfileEvent

    data class MenuItemClicked(val item: MenuItem) : ProfileEvent
}
