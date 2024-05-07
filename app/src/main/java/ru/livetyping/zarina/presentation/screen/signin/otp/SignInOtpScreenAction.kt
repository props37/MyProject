package ru.livetyping.zarina.presentation.screen.signin.otp

sealed class SignInOtpScreenAction {
    data object ScreenClosed : SignInOtpScreenAction()

    data object UserSignedIn : SignInOtpScreenAction()
}
