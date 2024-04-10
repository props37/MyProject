package ru.livetyping.zarina.domain.authorization

import ru.livetyping.zarina.domain.common.Token

data class AuthorizationTokens(
    val accessToken: Token,
    val refreshToken: Token,
)
