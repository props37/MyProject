package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone.model

internal sealed interface SignInByPhonePhoneConfirmationEvent {
    data object BackClicked : SignInByPhonePhoneConfirmationEvent

    data object OtpEntered : SignInByPhonePhoneConfirmationEvent

    data object RequestNewOtpClicked : SignInByPhonePhoneConfirmationEvent
}
