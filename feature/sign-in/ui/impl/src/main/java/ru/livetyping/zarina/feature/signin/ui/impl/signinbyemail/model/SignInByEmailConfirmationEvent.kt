package ru.livetyping.zarina.feature.signin.ui.impl.signinbyemail.model

internal sealed interface SignInByEmailConfirmationEvent {
    data object BackClicked :
        ru.livetyping.zarina.feature.signin.ui.impl.signinbyemail.model.SignInByEmailConfirmationEvent

    data object OtpEntered :
        ru.livetyping.zarina.feature.signin.ui.impl.signinbyemail.model.SignInByEmailConfirmationEvent

    data object RequestNewOtpClicked :
        ru.livetyping.zarina.feature.signin.ui.impl.signinbyemail.model.SignInByEmailConfirmationEvent
}
