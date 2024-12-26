package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation.model

internal sealed interface PhoneChangeConfirmationEvent {
    data object BackClicked : PhoneChangeConfirmationEvent

    data object OtpEntered : PhoneChangeConfirmationEvent

    data object RequestNewOtpClicked : PhoneChangeConfirmationEvent
}
