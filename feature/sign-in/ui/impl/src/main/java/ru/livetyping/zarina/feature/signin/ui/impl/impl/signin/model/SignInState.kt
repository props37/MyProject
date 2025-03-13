package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable

@Stable
internal data class SignInState(
    val signInByEmailState: SignInByEmailState,
    val phoneTextFieldState: TextFieldState,
    val isPhoneInvalid: Boolean,
    val isSignInButtonLoading: Boolean,
)
