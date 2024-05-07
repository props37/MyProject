package ru.livetyping.zarina.presentation.common.otp

import kotlin.time.Duration

sealed class OtpResendState {
    data object ResendAvailable : OtpResendState()

    data class TimeoutCountdown(val remainingTime: Duration) : OtpResendState()
}
