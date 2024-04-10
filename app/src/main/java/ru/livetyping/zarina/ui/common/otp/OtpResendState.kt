package ru.livetyping.zarina.ui.common.otp

import kotlin.time.Duration

sealed class OtpResendState {
    data object ResendAvailable : OtpResendState()

    data class TimeoutCountdown(val remainingTime: Duration) : OtpResendState()
}
