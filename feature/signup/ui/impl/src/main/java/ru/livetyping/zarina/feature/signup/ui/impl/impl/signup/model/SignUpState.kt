package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptcha

@Stable
internal data class SignUpState(
    val nameTextFieldState: TextFieldState,
    val isNameInvalid: Boolean,
    val birthDateEpochMillis: Long?,
    val isBirthDateInvalid: Boolean,
    val emailTextFieldState: TextFieldState,
    val isEmailInvalid: Boolean,
    val phoneTextFieldState: TextFieldState,
    val isPhoneInvalid: Boolean,
    val passwordTextFieldState: TextFieldState,
    val isPasswordInvalid: Boolean,
    val receiveEmails: Boolean,
    val receiveSms: Boolean,
    val arePoliciesAccepted: Boolean,
    val arePoliciesInvalid: Boolean,
    val isSignUpButtonLoading: Boolean,
    val visibleYandexCaptcha: YandexCaptcha?,
)
