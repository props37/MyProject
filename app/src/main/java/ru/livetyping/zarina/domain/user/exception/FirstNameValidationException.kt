package ru.livetyping.zarina.domain.user.exception

import ru.livetyping.zarina.domain.common.exception.ValidationException

abstract class FirstNameValidationException(message: String) : ValidationException(message)

open class InvalidFirstNameException(message: String = "Invalid first name") :
    FirstNameValidationException(message)

class EmptyFirstNameException(message: String = "First name can not be empty") :
    InvalidFirstNameException(message)
