package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone.model

internal sealed interface SignInByPhoneConfirmationEvent {
    data object BackClicked : SignInByPhoneConfirmationEvent

    data object OtpEntered : SignInByPhoneConfirmationEvent

    data object RequestNewOtpClicked : SignInByPhoneConfirmationEvent
}
