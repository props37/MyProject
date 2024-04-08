package ru.livetyping.zarina.domain.common.exception

abstract class OtpException(message: String) : Exception(message)

class OtpTimeoutException(message: String = "OTP timeout") : OtpException(message)

class InvalidOtpException(message: String = "OTP is invalid") : OtpException(message)
