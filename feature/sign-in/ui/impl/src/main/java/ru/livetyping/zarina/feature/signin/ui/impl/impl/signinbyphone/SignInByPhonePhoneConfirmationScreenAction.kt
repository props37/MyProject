package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone

internal sealed interface SignInByPhonePhoneConfirmationScreenAction {
    data object BackClicked : SignInByPhonePhoneConfirmationScreenAction

    data object PhoneConfirmed : SignInByPhonePhoneConfirmationScreenAction
}
