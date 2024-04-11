package ru.livetyping.zarina.ui.screen.signin

sealed class SignInScreenAction {
    data object ScreenClosed : SignInScreenAction()

    data object SignUpClicked : SignInScreenAction()

    data object ForgotPasswordClicked : SignInScreenAction()

    data object UserSignedIn : SignInScreenAction()
}
