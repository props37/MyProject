package ru.livetyping.zarina.core.uicommon.otp

import kotlin.time.Duration

// Marked as stable on config/compose/stability_config.txt
public sealed class NewOtpRequestState {
    // Marked as stable on config/compose/stability_config.txt
    public data object Available : NewOtpRequestState()

    // Marked as stable on config/compose/stability_config.txt
    public data class Unavailable(val timeout: Duration) : NewOtpRequestState()
}
