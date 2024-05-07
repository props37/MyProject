package ru.livetyping.zarina.presentation.screen.signin.passwordrecovery

sealed class PasswordRecoveryScreenAction {
    data object ScreenClosed : PasswordRecoveryScreenAction()

    data object PasswordResetRequested : PasswordRecoveryScreenAction()
}
