package ru.livetyping.zarina.core.domain.model.user.exception

public open class OtpException(message: String) : Exception(message)

public class OtpTimeoutException(message: String = "OTP timeout exceeded") : OtpException(message)

public class InvalidOtpException(message: String = "OTP is invalid") : OtpException(message)
