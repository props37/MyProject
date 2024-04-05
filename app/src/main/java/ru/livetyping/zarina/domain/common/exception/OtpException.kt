package ru.livetyping.zarina.domain.common.exception

abstract class OtpException(message: String) : Exception(message)

class OtpTimeoutException(message: String = "OTP timeout") : OtpException(message)
