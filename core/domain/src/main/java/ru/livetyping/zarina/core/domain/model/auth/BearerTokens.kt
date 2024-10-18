package ru.livetyping.zarina.core.domain.model.auth

import ru.livetyping.zarina.core.domain.model.common.Token

public data class BearerTokens(
    val accessToken: Token,
    val refreshToken: Token,
)
