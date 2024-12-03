package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model

internal sealed class SignOutDialogState {
    data class Visible(val isSignOutButtonLoading: Boolean) : SignOutDialogState()

    data object Hidden : SignOutDialogState()
}
