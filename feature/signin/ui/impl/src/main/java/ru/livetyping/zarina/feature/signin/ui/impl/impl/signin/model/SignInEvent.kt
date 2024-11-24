package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model

internal sealed interface SignInEvent {
    data object BackClicked : SignInEvent

    data object ForgotPasswordClicked : SignInEvent

    data object SignInClicked : SignInEvent

    data object SignUpClicked : SignInEvent
}
