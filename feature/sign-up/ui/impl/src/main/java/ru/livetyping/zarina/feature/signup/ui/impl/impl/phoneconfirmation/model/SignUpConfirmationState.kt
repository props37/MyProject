package ru.livetyping.zarina.feature.signup.ui.impl.impl.phoneconfirmation.model

import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.uicompose.otp.TextFieldOtpState

@Stable
internal data class SignUpConfirmationState(
    val phone: PhoneNumber,
    val otpState: TextFieldOtpState,
    val isRequestNewOtpButtonLoading: Boolean,
)
