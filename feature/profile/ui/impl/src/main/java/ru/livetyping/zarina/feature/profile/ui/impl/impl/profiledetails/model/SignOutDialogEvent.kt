package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model

internal sealed interface SignOutDialogEvent {
    data object SignOutClicked : SignOutDialogEvent

    data object DismissRequested : SignOutDialogEvent
}
