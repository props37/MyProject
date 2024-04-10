package ru.livetyping.zarina.ui.screen.signup.otp

sealed class SignUpOtpScreenAction {
    data object ScreenClosed : SignUpOtpScreenAction()

    data object SignUpConfirmed : SignUpOtpScreenAction()
}
