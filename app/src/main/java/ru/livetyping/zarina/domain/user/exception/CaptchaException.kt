package ru.livetyping.zarina.domain.user.exception

abstract class CaptchaException(message: String) : Exception(message)

class InvalidCaptchaException(message: String = "Invalid captcha") : CaptchaException(message)
