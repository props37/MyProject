package ru.livetyping.zarina.core.domain.model.user.exception

public open class PasswordException(message: String) : Exception(message)

public open class InvalidPasswordException(message: String = "Invalid password") :
    PasswordException(message)

public class EmptyPasswordException(message: String = "Password can not be empty") :
    InvalidPasswordException(message)

public class PasswordTooShortException(message: String = "Password is too short") :
    InvalidPasswordException(message)
