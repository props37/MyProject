package ru.livetyping.zarina.presentation.screen.profile.details.accountdeletionconfirmation

sealed class AccountDeletionConfirmationScreenAction {
    data object ScreenClosed : AccountDeletionConfirmationScreenAction()

    data object AccountDeleted : AccountDeletionConfirmationScreenAction()
}
