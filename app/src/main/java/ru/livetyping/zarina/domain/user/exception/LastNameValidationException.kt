package ru.livetyping.zarina.domain.user.exception

import ru.livetyping.zarina.domain.common.exception.ValidationException

abstract class LastNameValidationException(message: String) : ValidationException(message)

open class InvalidLastNameException(
    message: String = "Invalid last name",
    private val localizedMessage: String? = null,
) : LastNameValidationException(message) {
    override fun getLocalizedMessage(): String? = localizedMessage ?: message
}

class EmptyLastNameException(message: String = "Last name can not be empty") :
    InvalidLastNameException(message)
