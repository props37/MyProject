package ru.livetyping.zarina.core.domain.model.user.exception

public open class LastNameException(message: String) : Exception(message)

public open class InvalidLastNameException(message: String = "Invalid last name") :
    LastNameException(message)

public class EmptyLastNameException(message: String = "Last name can not be empty") :
    InvalidLastNameException(message)
