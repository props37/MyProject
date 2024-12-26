package ru.livetyping.zarina.core.domain.model.user.exception

public open class PhoneNumberException(message: String) : Exception(message)

public open class InvalidPhoneNumberException(message: String = "Invalid phone") :
    PhoneNumberException(message)

public class EmptyPhoneNumberException(message: String = "Phone can not be empty") :
    InvalidPhoneNumberException(message)

public class PhoneNumberAlreadyUsedException(message: String = "Phone is already used") :
    PhoneNumberException(message)
