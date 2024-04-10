package ru.livetyping.zarina.ui.screen.signupotp

sealed class SignUpOtpScreenAction {
    data object ScreenClosed : SignUpOtpScreenAction()

    data object SignUpConfirmed : SignUpOtpScreenAction()
}
