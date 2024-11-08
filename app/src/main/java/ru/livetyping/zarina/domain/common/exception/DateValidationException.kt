package ru.livetyping.zarina.domain.common.exception

abstract class DateValidationException(message: String) : ValidationException(message)

open class InvalidDateException(message: String = "Invalid date") : DateValidationException(message)

class EmptyDateException(message: String = "Date can not be empty") : InvalidDateException(message)
