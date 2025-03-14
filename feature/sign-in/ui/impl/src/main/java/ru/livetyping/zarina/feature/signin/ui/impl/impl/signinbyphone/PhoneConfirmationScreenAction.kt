package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone

internal sealed interface PhoneConfirmationScreenAction {
    data object BackClicked : PhoneConfirmationScreenAction

    data object PhoneConfirmed : PhoneConfirmationScreenAction
}
