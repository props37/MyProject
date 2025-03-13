package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable

@Stable
internal sealed class SignInByEmailState {

    @Stable
    data class Main(
        val emailTextFieldState: TextFieldState,
        val isEmailInvalid: Boolean,
        val passwordTextFieldState: TextFieldState,
        val isPasswordInvalid: Boolean,
    ) : SignInByEmailState()

    @Stable
    data class PhoneConfirmation(
        val phoneTextFieldState: TextFieldState,
        val isPhoneInvalid: Boolean,
        val isGetConfirmationCodeButtonLoading: Boolean,
    ) : SignInByEmailState()
}
