package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails

internal sealed interface ProfileDetailsScreenAction {
    data object BackClicked : ProfileDetailsScreenAction

    data object ChangePhoneClicked : ProfileDetailsScreenAction

    data object ChangeEmailClicked : ProfileDetailsScreenAction

    data object UserSignedOut : ProfileDetailsScreenAction

    data object AccountDeleted : ProfileDetailsScreenAction
}
