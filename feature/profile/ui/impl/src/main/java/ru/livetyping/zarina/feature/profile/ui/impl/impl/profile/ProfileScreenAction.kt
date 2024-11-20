package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile

internal sealed interface ProfileScreenAction {
    data object ScreenClosed : ProfileScreenAction
}
