package ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable

@Stable
internal data class EmailChangeState(
    val emailTextFieldState: TextFieldState,
    val isEmailInvalid: Boolean,
    val isChangeEmailButtonLoading: Boolean,
)
