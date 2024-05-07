package ru.livetyping.zarina.presentation.screen.signup.otp

sealed class SignUpOtpScreenAction {
    data object ScreenClosed : SignUpOtpScreenAction()

    data object SignUpConfirmed : SignUpOtpScreenAction()
}
