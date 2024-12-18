package ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation

internal sealed interface PhoneConfirmationScreenAction {
    data object BackClicked : PhoneConfirmationScreenAction
}
