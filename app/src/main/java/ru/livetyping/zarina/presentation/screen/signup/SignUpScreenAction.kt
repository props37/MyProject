package ru.livetyping.zarina.presentation.screen.signup

import ru.livetyping.zarina.domain.common.PhoneNumber

sealed class SignUpScreenAction {
    data object ScreenClosed : SignUpScreenAction()

    data class UserCreated(val phone: PhoneNumber) : SignUpScreenAction()
}
