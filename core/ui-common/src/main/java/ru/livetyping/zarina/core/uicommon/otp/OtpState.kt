package ru.livetyping.zarina.core.uicommon.otp

public abstract class OtpState {
    public abstract val otp: String
    public abstract val isLoading: Boolean
    public abstract val isInvalid: Boolean
    public abstract val newOtpRequestState: NewOtpRequestState
    public abstract val isRequestNewOtpButtonLoading: Boolean
}
