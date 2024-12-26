package ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging

internal sealed interface PasswordChangeScreenAction {
    data object BackClicked : PasswordChangeScreenAction

    data object PasswordChanged : PasswordChangeScreenAction
}
