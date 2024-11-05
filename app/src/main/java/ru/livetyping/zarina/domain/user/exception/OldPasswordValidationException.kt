package ru.livetyping.zarina.domain.user.exception

import ru.livetyping.zarina.domain.common.exception.ValidationException

abstract class OldPasswordValidationException(message: String) : ValidationException(message)

open class InvalidOldPasswordException(message: String = "Invalid password") :
    OldPasswordValidationException(message)

class EmptyOldPasswordException(message: String = "Password can not be empty") :
    InvalidOldPasswordException(message)
