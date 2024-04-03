package ru.livetyping.zarina.domain.user.exception

import ru.livetyping.zarina.domain.common.exception.ValidationException

abstract class EmailException(message: String) : ValidationException(message)

open class InvalidEmailException(message: String = "Invalid email address") :
    EmailException(message)

class EmptyEmailException(message: String = "Email can not be empty") :
    InvalidEmailException(message)

class EmailAlreadyInUseException(message: String = "Email is already in use") :
    EmailException(message)
