package ru.zarina.zarina.domain.old

sealed interface AuthorizationToken {

    val token: String

    @JvmInline
    value class Device(override val token: String) : AuthorizationToken

}
