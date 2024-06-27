package ru.livetyping.zarina.presentation.screen.profile.details.changepassword

sealed class ChangePasswordScreenAction {
    data object ScreenClosed : ChangePasswordScreenAction()

    data object PasswordChanged : ChangePasswordScreenAction()
}
