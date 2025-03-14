package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail.model

internal sealed interface SignInByEmailPhoneConfirmationEvent {
    data object BackClicked : SignInByEmailPhoneConfirmationEvent

    data object OtpEntered : SignInByEmailPhoneConfirmationEvent

    data object RequestNewOtpClicked : SignInByEmailPhoneConfirmationEvent
}
