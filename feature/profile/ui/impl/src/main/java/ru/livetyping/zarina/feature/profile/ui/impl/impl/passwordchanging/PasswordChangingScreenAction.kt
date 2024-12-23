package ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging

internal sealed interface PasswordChangingScreenAction {
    data object BackClicked : PasswordChangingScreenAction
}
