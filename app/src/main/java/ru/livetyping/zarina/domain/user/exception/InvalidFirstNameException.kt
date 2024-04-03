package ru.livetyping.zarina.domain.user.exception

import ru.livetyping.zarina.domain.common.exception.ValidationException

open class InvalidFirstNameException(message: String = "Invalid first name") :
    ValidationException(message)

class EmptyFirstNameException(message: String = "First name can not be empty") :
    InvalidFirstNameException(message)
