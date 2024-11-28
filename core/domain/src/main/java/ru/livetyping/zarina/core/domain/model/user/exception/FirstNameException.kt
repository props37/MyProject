package ru.livetyping.zarina.core.domain.model.user.exception

public open class FirstNameException(message: String) : Exception(message)

public open class InvalidFirstNameException(message: String = "Invalid first name") :
    FirstNameException(message)

public class EmptyFirstNameException(message: String = "First name can not be empty") :
    InvalidFirstNameException(message)
