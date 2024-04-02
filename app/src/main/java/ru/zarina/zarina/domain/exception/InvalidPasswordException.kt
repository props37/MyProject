package ru.zarina.zarina.domain.exception

import ru.zarina.zarina.domain.common.exception.ValidationException

open class InvalidPasswordException(message: String = "Invalid password") :
    ValidationException(message)

class EmptyPasswordException(message: String = "Password can not be empty") :
    InvalidPasswordException(message)

class PasswordTooShortException(message: String = "Password is too short") :
    InvalidPasswordException(message)
