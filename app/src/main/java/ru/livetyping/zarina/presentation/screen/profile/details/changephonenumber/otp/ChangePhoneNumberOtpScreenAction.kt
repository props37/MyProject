package ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber.otp

sealed class ChangePhoneNumberOtpScreenAction {
    data object ScreenClosed : ChangePhoneNumberOtpScreenAction()

    data object PhoneNumberChanged : ChangePhoneNumberOtpScreenAction()
}
