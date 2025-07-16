package ru.livetyping.zarina.feature.profile.ui.impl.phonechangeconfirmation.model

internal sealed interface PhoneChangeConfirmationEvent {
    data object BackClicked :
        ru.livetyping.zarina.feature.profile.ui.impl.phonechangeconfirmation.model.PhoneChangeConfirmationEvent

    data object OtpEntered :
        ru.livetyping.zarina.feature.profile.ui.impl.phonechangeconfirmation.model.PhoneChangeConfirmationEvent

    data object RequestNewOtpClicked :
        ru.livetyping.zarina.feature.profile.ui.impl.phonechangeconfirmation.model.PhoneChangeConfirmationEvent
}
