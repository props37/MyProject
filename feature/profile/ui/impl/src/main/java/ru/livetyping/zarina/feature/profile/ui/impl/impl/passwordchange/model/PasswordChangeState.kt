package ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchange.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable

@Stable
internal data class PasswordChangeState(
    val oldPasswordTextFieldState: TextFieldState,
    val newPasswordTextFieldState: TextFieldState,
    val isOldPasswordInvalid: Boolean,
    val isNewPasswordInvalid: Boolean,
    val isChangePasswordButtonLoading: Boolean,
)
