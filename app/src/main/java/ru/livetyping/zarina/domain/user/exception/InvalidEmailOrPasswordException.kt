package ru.livetyping.zarina.domain.user.exception

class InvalidEmailOrPasswordException(message: String = "Invalid email or password") :
    Exception(message)
