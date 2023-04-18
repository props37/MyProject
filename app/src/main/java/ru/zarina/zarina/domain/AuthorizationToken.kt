package ru.zarina.zarina.domain

sealed interface AuthorizationToken {

    @JvmInline
    value class Device(val token: String) : AuthorizationToken

}
