package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail

internal sealed interface SignInByEmailPhoneConfirmationScreenAction {
    data object BackClicked : SignInByEmailPhoneConfirmationScreenAction

    data object SignInConfirmed : SignInByEmailPhoneConfirmationScreenAction
}
