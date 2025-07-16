package ru.livetyping.zarina.feature.profile.ui.impl.passwordchange.model

internal sealed interface PasswordChangeEvent {
    data object BackClicked : PasswordChangeEvent

    data object ChangePasswordClicked : PasswordChangeEvent
}
