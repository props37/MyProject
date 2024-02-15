package ru.zarina.zarina.domain.rework.user.exception

open class EmailException(message: String) : Exception(message)

class EmptyEmailException(message: String = "Email can not be empty") : EmailException(message)

class InvalidEmailException(message: String = "Invalid email address") : EmailException(message)
