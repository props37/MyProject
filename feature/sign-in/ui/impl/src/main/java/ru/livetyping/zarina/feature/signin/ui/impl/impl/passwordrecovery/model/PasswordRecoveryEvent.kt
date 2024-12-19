package ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery.model

internal sealed interface PasswordRecoveryEvent {
    data object BackClicked : PasswordRecoveryEvent
}
