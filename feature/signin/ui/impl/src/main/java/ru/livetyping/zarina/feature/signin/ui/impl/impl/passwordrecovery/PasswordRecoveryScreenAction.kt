package ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery

internal sealed interface PasswordRecoveryScreenAction {
    data object ScreenClosed : PasswordRecoveryScreenAction
}
