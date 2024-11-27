package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.model

internal sealed interface SignUpEvent {
    data object BackClicked : SignUpEvent

    data class ReceiveEmailsChanged(val receive: Boolean) : SignUpEvent

    data class ReceiveSmsChanged(val receive: Boolean) : SignUpEvent

    data object SignUpClicked : SignUpEvent
}
