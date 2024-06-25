package ru.livetyping.zarina.presentation.screen.profile.details.changeemail

sealed class ChangeEmailScreenAction {
    data object ScreenClosed : ChangeEmailScreenAction()

    data object EmailChanged : ChangeEmailScreenAction()
}
