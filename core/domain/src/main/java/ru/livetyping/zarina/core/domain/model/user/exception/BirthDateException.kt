package ru.livetyping.zarina.core.domain.model.user.exception

public open class BirthDateException(message: String) : Exception()

public open class InvalidBirthDateException(message: String = "Invalid birth date") :
    BirthDateException(message)

public class EmptyBirthDateException(message: String = "Birth date can not be empty") :
    InvalidBirthDateException(message)
