package ru.livetyping.zarina.core.domain.model.user.exception

// TODO: [High] Rename to PhoneNumberException
public open class PhoneException(message: String) : Exception(message)

public open class InvalidPhoneException(message: String = "Invalid phone") : PhoneException(message)

public class EmptyPhoneException(message: String = "Phone can not be empty") :
    InvalidPhoneException(message)

public class PhoneNumberAlreadyUsedException(message: String = "Phone is already used") :
    PhoneException(message)
