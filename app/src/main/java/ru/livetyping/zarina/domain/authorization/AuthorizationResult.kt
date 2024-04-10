package ru.livetyping.zarina.domain.authorization

import ru.livetyping.zarina.domain.user.User

data class AuthorizationResult(
    val tokens: AuthorizationTokens,
    val user: User,
)
