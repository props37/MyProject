package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone.model

import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.uicompose.otp.TextFieldOtpState

@Stable
internal data class SignInByPhoneConfirmationState(
    val phone: PhoneNumber,
    val otpState: TextFieldOtpState,
)
