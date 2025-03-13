package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

internal sealed interface SignInScreenAction {
    data object BackClicked : SignInScreenAction

    data object UserSignedIn : SignInScreenAction

    data class SignInByPhoneRequested(val phone: PhoneNumber) : SignInScreenAction

    data object ForgotPasswordClicked : SignInScreenAction

    data object SignUpClicked : SignInScreenAction

    data class PhoneConfirmationNeeded(val phone: PhoneNumber) : SignInScreenAction
}
