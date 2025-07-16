package ru.livetyping.zarina.feature.profile.ui.impl.passwordchange

internal sealed interface PasswordChangeScreenAction {
    data object BackClicked : PasswordChangeScreenAction

    data object PasswordChanged : PasswordChangeScreenAction
}
