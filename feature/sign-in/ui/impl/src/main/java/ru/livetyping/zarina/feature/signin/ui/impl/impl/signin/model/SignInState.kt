package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptcha

@Stable
internal data class SignInState(
    val emailTextFieldState: TextFieldState,
    val isEmailInvalid: Boolean,
    val passwordTextFieldState: TextFieldState,
    val isPasswordInvalid: Boolean,
    val phoneTextFieldState: TextFieldState,
    val isPhoneInvalid: Boolean,
    val isSignInButtonLoading: Boolean,
    val visibleYandexCaptcha: YandexCaptcha?,
)
