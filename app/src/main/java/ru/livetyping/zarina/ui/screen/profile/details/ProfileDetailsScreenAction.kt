package ru.livetyping.zarina.ui.screen.profile.details

sealed class ProfileDetailsScreenAction {
    data object ScreenClosed : ProfileDetailsScreenAction()

    data object SignOutClicked : ProfileDetailsScreenAction()
}
