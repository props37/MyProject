package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail

internal sealed interface SignInByEmailConfirmationScreenAction {
    data object BackClicked : SignInByEmailConfirmationScreenAction

    data object SignInConfirmed : SignInByEmailConfirmationScreenAction
}
