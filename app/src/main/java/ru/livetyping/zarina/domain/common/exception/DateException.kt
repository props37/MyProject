package ru.livetyping.zarina.domain.common.exception

abstract class DateException(message: String) : ValidationException(message)

open class InvalidDateException(message: String = "Invalid date") : DateException(message)

class EmptyDateException(message: String = "Date can not be empty") : InvalidDateException(message)
