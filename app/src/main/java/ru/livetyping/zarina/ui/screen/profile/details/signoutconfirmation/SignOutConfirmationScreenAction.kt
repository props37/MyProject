package ru.livetyping.zarina.ui.screen.profile.details.signoutconfirmation

sealed class SignOutConfirmationScreenAction {
    data object ScreenClosed : SignOutConfirmationScreenAction()

    data object UserSignedOut : SignOutConfirmationScreenAction()
}
