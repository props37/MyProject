package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone

internal sealed interface SignInByPhoneConfirmationScreenAction {
    data object BackClicked : SignInByPhoneConfirmationScreenAction

    data object SignInConfirmed : SignInByPhoneConfirmationScreenAction
}
