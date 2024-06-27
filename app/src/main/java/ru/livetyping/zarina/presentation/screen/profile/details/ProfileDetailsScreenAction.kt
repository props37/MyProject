package ru.livetyping.zarina.presentation.screen.profile.details

sealed class ProfileDetailsScreenAction {
    data object ScreenClosed : ProfileDetailsScreenAction()

    data object ChangePasswordClicked : ProfileDetailsScreenAction()

    data object ChangeEmailClicked : ProfileDetailsScreenAction()

    data object ChangePhoneNumberClicked : ProfileDetailsScreenAction()

    data object SignOutClicked : ProfileDetailsScreenAction()

    data object DeleteAccountClicked : ProfileDetailsScreenAction()
}
