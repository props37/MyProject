package ru.livetyping.zarina.domain.user.exception

import ru.livetyping.zarina.domain.common.exception.ValidationException

abstract class LastNameException(message: String) : ValidationException(message)

open class InvalidLastNameException(message: String = "Invalid last name") :
    LastNameException(message)

class EmptyLastNameException(message: String = "Last name can not be empty") :
    InvalidLastNameException(message)
