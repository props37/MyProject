package ru.livetyping.zarina.core.domain.model.auth

import ru.livetyping.zarina.core.domain.model.common.Token

// Marked as stable on config/compose/stability_config.txt
public data class BearerTokens(
    val accessToken: Token,
    val refreshToken: Token,
)
