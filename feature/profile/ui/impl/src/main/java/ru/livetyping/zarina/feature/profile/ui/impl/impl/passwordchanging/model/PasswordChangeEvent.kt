package ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging.model

internal sealed interface PasswordChangeEvent {
    data object BackClicked : PasswordChangeEvent

    data object ChangePasswordClicked : PasswordChangeEvent
}
