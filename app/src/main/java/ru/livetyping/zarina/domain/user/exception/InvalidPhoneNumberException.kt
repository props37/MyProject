package ru.livetyping.zarina.domain.user.exception

import ru.livetyping.zarina.domain.common.exception.ValidationException

open class InvalidPhoneNumberException(message: String = "Invalid phone number") :
    ValidationException(message)

class EmptyPhoneNumberException(message: String = "Phone number can not be empty") :
    InvalidPhoneNumberException(message)
