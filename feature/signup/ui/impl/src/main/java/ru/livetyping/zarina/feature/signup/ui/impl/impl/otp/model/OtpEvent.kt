package ru.livetyping.zarina.feature.signup.ui.impl.impl.otp.model

internal sealed interface OtpEvent {
    data object BackClicked : OtpEvent

    data object OtpEntered : OtpEvent

    data object RequestNewOtpClicked : OtpEvent
}
