package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation

internal sealed interface PhoneChangeConfirmationScreenAction {
    data object BackClicked : PhoneChangeConfirmationScreenAction

    data object PhoneChangeConfirmed : PhoneChangeConfirmationScreenAction
}
