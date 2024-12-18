package ru.livetyping.zarina.feature.signup.ui.impl.impl.otp.model

internal sealed interface PhoneConfirmationEvent {
    data object BackClicked : PhoneConfirmationEvent

    data object OtpEntered : PhoneConfirmationEvent

    data object RequestNewOtpClicked : PhoneConfirmationEvent
}
