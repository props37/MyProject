package ru.livetyping.zarina.domain.user.exception

import ru.livetyping.zarina.domain.common.exception.ValidationException

open class InvalidEmailException(message: String = "Invalid email address") :
    ValidationException(message)

class EmptyEmailException(message: String = "Email can not be empty") :
    InvalidEmailException(message)
