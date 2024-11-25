package ru.livetyping.zarina.core.domain.model.user

import ru.livetyping.zarina.core.domain.model.common.Email

public sealed class SignInParams

public data class SignInByEmailParams(
    val email: Email,
    val password: String,
) : SignInParams()
