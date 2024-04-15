package ru.livetyping.zarina.ui.screen.signin.otp

sealed class SignInOtpScreenAction {
    data object ScreenClosed : SignInOtpScreenAction()

    data object UserSignedIn : SignInOtpScreenAction()
}
