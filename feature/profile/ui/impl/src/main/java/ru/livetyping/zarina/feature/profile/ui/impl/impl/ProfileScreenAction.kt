package ru.livetyping.zarina.feature.profile.ui.impl.impl

internal sealed interface ProfileScreenAction {
    data object ScreenClosed : ProfileScreenAction
}
