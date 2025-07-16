package ru.livetyping.zarina.feature.signup.ui.impl.phoneconfirmation.model

internal sealed interface SignUpConfirmationEvent {
    data object BackClicked : SignUpConfirmationEvent

    data object OtpEntered : SignUpConfirmationEvent

    data object RequestNewOtpClicked : SignUpConfirmationEvent
}
