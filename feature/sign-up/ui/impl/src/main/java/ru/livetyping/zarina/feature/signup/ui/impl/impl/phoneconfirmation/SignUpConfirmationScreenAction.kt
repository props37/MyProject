package ru.livetyping.zarina.feature.signup.ui.impl.impl.phoneconfirmation

internal sealed interface SignUpConfirmationScreenAction {
    data object BackClicked : SignUpConfirmationScreenAction

    data object SignUpConfirmed : SignUpConfirmationScreenAction
}
