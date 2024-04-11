package ru.livetyping.zarina.ui.screen.signin.passwordrecovery

sealed class PasswordRecoveryScreenAction {
    data object ScreenClosed : PasswordRecoveryScreenAction()
}
