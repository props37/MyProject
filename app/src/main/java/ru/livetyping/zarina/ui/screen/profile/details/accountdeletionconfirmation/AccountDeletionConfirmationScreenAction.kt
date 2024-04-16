package ru.livetyping.zarina.ui.screen.profile.details.accountdeletionconfirmation

sealed class AccountDeletionConfirmationScreenAction {
    data object ScreenClosed : AccountDeletionConfirmationScreenAction()

    data object AccountDeleted : AccountDeletionConfirmationScreenAction()
}
