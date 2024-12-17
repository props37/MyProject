package ru.livetyping.zarina.feature.signup.ui.impl.impl.otp

internal sealed interface OtpScreenAction {
    data object BackClicked : OtpScreenAction

    data object SignUpConfirmed : OtpScreenAction
}
