package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model

internal sealed interface AccountDeletionDialogEvent {
    data object DeleteAccountClicked : AccountDeletionDialogEvent

    data object DismissRequested : AccountDeletionDialogEvent
}
