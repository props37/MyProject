package ru.zarina.zarina.domain.authorization

import ru.zarina.zarina.domain.common.Token

data class AuthorizationTokens(
    val accessToken: Token,
    val refreshToken: Token,
)
