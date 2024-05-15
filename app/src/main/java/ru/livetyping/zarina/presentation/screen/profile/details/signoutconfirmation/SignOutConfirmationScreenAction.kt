package ru.livetyping.zarina.presentation.screen.profile.details.signoutconfirmation

sealed class SignOutConfirmationScreenAction {
    data object ScreenClosed : SignOutConfirmationScreenAction()

    data object UserSignedOut : SignOutConfirmationScreenAction()
}
