package ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation

internal sealed interface PhoneConfirmationEvent {
    data object BackClicked : PhoneConfirmationEvent

    data object OtpEntered : PhoneConfirmationEvent

    data object RequestNewOtpClicked : PhoneConfirmationEvent
}
