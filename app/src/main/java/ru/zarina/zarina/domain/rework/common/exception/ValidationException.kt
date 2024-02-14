package ru.zarina.zarina.domain.rework.common.exception

class ValidationException(
    message: String = "Validation failed",
    val exceptions: List<Throwable>? = null,
) : Exception(message)
