package ru.livetyping.zarina.core.domain.model.user.exception

public open class EmailException(message: String) : Exception(message)

public class InvalidEmailException(message: String = "Invalid email") : EmailException(message)

public class EmptyEmailException(message: String = "Email can not be empty") :
    EmailException(message)

public class EmailAlreadyUsed(message: String = "Email is already used") : EmailException(message)
