package ru.livetyping.zarina.core.domain.model.user

import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

public data class AuthResult(
    val tokens: BearerTokens,
    val user: User,
    val phoneConfirmation: PhoneConfirmation?,
) {
    public fun isPhoneConfirmationNeeded(): Boolean {
        return phoneConfirmation?.isConfirmed == false
    }

    public data class PhoneConfirmation(
        val phone: PhoneNumber?,
        val isConfirmed: Boolean,
    )
}
