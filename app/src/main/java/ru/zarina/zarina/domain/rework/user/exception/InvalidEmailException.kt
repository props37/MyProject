package ru.zarina.zarina.domain.rework.user.exception

import ru.zarina.zarina.domain.common.exception.ValidationException

open class InvalidEmailException(message: String = "Invalid email address") :
    ValidationException(message)

class EmptyEmailException(message: String = "Email can not be empty") :
    InvalidEmailException(message)
