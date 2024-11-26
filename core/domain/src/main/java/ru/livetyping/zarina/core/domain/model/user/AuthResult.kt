package ru.livetyping.zarina.core.domain.model.user

import ru.livetyping.zarina.core.domain.model.auth.BearerTokens

public data class AuthResult(
    val tokens: BearerTokens,
    val user: User,
)
