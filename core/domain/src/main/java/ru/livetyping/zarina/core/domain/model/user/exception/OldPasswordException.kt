package ru.livetyping.zarina.core.domain.model.user.exception

public open class OldPasswordException(message: String) : Exception(message)

public open class InvalidOldPasswordException(message: String = "Invalid password") :
    OldPasswordException(message)

public class EmptyOldPasswordException(message: String = "Password can not be empty") :
    InvalidOldPasswordException(message)
