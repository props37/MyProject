package ru.livetyping.zarina.core.uicompose.otp

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.uicommon.otp.NewOtpRequestState
import ru.livetyping.zarina.core.uicommon.otp.OtpState

@Stable
public data class TextFieldOtpState(
    val textFieldState: TextFieldState,
    override val isLoading: Boolean,
    override val isInvalid: Boolean,
    override val newOtpRequestState: NewOtpRequestState,
    override val isRequestNewOtpButtonLoading: Boolean,
) : OtpState() {
    override val otp: String get() = textFieldState.text.toString()
}
