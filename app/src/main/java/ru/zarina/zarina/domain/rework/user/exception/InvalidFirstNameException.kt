package ru.zarina.zarina.domain.rework.user.exception

import ru.zarina.zarina.domain.rework.common.exception.ValidationException

open class InvalidFirstNameException(message: String = "Invalid first name") :
    ValidationException(message)

class EmptyFirstNameException(message: String = "First name can not be empty") :
    InvalidFirstNameException(message)
