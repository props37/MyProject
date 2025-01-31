package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechange.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable

@Stable
internal data class PhoneChangeState(
    val phoneTextFieldState: TextFieldState,
    val isPhoneInvalid: Boolean,
    val isRequestPhoneChangeButtonLoading: Boolean,
)
