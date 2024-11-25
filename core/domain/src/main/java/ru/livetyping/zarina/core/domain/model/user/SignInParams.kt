package ru.livetyping.zarina.core.domain.model.user

import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

public sealed class SignInParams

public data class SignInByEmailParams(
    val email: Email,
    val password: String,
) : SignInParams()

public data class SignInByPhoneParams(val phone: PhoneNumber) : SignInParams()
