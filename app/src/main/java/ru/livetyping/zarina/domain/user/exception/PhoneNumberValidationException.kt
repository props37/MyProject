package ru.livetyping.zarina.domain.user.exception

import ru.livetyping.zarina.domain.common.exception.ValidationException

abstract class PhoneNumberValidationException(message: String) : ValidationException(message)

open class InvalidPhoneNumberException(message: String = "Invalid phone number") :
    PhoneNumberValidationException(message)

class EmptyPhoneNumberException(message: String = "Phone number can not be empty") :
    InvalidPhoneNumberException(message)

class PhoneNumberAlreadyInUseException(message: String = "Phone number is already in use") :
    PhoneNumberValidationException(message)
