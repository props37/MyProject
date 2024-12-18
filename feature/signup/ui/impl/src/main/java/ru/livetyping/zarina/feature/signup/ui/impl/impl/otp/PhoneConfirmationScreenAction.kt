package ru.livetyping.zarina.feature.signup.ui.impl.impl.otp

internal sealed interface PhoneConfirmationScreenAction {
    data object BackClicked : PhoneConfirmationScreenAction

    data object PhoneConfirmed : PhoneConfirmationScreenAction
}
