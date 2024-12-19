package ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable

@Stable
internal data class PasswordRecoveryState(
    val emailTextFieldState: TextFieldState,
    val isEmailInvalid: Boolean,
    val isSendButtonLoading: Boolean,
)
