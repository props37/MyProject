package ru.zarina.zarina.domain.exception.validation

open class ValidationException(message: String) : IllegalArgumentException(message)

open class LengthException(message: String) : ValidationException(message)

class EmptyException(message: String) : LengthException(message)

class TooLongException(message: String, val maxLength: Long) : LengthException(message)

class FormatException(message: String) : ValidationException(message)

class IllegalContentsException(message: String) : ValidationException(message)
