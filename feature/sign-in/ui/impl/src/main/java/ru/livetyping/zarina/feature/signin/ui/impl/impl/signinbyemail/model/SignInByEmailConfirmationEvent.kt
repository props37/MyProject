package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail.model

internal sealed interface SignInByEmailConfirmationEvent {
    data object BackClicked : SignInByEmailConfirmationEvent

    data object OtpEntered : SignInByEmailConfirmationEvent

    data object RequestNewOtpClicked : SignInByEmailConfirmationEvent
}
