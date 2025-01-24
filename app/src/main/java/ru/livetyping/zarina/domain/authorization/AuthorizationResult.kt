package ru.livetyping.zarina.domain.authorization

import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.user.User

data class AuthorizationResult(
    val tokens: AuthorizationTokens,
    val user: User,
    val phoneConfirmation: PhoneConfirmation?,
) {
    data class PhoneConfirmation(
        val phone: PhoneNumber?,
        val isConfirmed: Boolean,
    )
}
