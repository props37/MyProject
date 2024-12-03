package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model

internal sealed class AccountDeletionDialogState {
    data class Visible(val isDeleteButtonLoading: Boolean) : AccountDeletionDialogState()

    data object Hidden : AccountDeletionDialogState()
}
