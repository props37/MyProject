package ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation.model

import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptcha
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.uicompose.otp.TextFieldOtpState

@Stable
internal data class PhoneConfirmationState(
    val phone: PhoneNumber,
    val otpState: TextFieldOtpState,
    val isRequestNewOtpButtonLoading: Boolean,
    val visibleYandexCaptcha: YandexCaptcha?,
)
