package ru.livetyping.zarina.presentation.screen.signin.phoneconfirmation

sealed class PhoneNumberConfirmationScreenAction {
    data object ScreenClosed : PhoneNumberConfirmationScreenAction()

    data object PhoneNumberConfirmed : PhoneNumberConfirmationScreenAction()
}
