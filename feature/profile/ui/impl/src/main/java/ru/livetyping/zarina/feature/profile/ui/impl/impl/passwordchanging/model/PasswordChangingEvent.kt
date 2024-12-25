package ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging.model

internal sealed interface PasswordChangingEvent {
    data object BackClicked : PasswordChangingEvent

    data object ChangePasswordClicked : PasswordChangingEvent
}
