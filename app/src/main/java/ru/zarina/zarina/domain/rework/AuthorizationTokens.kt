package ru.zarina.zarina.domain.rework

import ru.zarina.zarina.domain.rework.common.Token

data class AuthorizationTokens(
    val accessToken: Token,
    val refreshToken: Token,
)
