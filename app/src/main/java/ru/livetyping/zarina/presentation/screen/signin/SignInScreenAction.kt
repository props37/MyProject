package ru.livetyping.zarina.presentation.screen.signin

import ru.livetyping.zarina.domain.common.PhoneNumber

sealed class SignInScreenAction {
    data object ScreenClosed : SignInScreenAction()

    data object SignUpClicked : SignInScreenAction()

    data object ForgotPasswordClicked : SignInScreenAction()

    data class SignInByPhoneRequested(val phone: PhoneNumber) : SignInScreenAction()

    data object UserSignedIn : SignInScreenAction()

    data class PhoneConfirmationNeeded(val phone: PhoneNumber) : SignInScreenAction()
}
