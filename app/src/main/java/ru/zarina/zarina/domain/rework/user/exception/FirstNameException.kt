package ru.zarina.zarina.domain.rework.user.exception

open class FirstNameException(message: String) : Exception(message)

class EmptyFirstNameException(message: String = "First name can not be empty") :
    FirstNameException(message)

class InvalidFirstNameException(message: String = "Invalid first name") :
    FirstNameException(message)
