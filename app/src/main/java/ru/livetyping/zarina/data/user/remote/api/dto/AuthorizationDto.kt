package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.authorization.remote.api.dto.AuthorizationTokensDto
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.UserDto
import ru.livetyping.zarina.domain.authorization.AuthorizationResult
import ru.livetyping.zarina.domain.common.PhoneNumber

@Serializable
data class AuthorizationDto(
    @SerialName("user")
    val user: UserDto? = null,

    @SerialName("jwt")
    val authorizationTokens: AuthorizationTokensDto? = null,

    @SerialName("phone_verification")
    val phoneVerification: PhoneConfirmationDto? = null,
) {
    fun toAuthorizationResult(): AuthorizationResult {
        checkNotNull(authorizationTokens) { "authorizationTokens is null" }
        checkNotNull(user) { "user is null" }
        return AuthorizationResult(
            tokens = authorizationTokens.toAuthorizationTokens(),
            user = user.toUser(),
            phoneConfirmation = phoneVerification?.toPhoneConfirmation(),
        )
    }

    @Serializable
    data class PhoneConfirmationDto(
        @SerialName("phone_is_verified")
        val isPhoneVerified: Boolean? = null,

        @SerialName("unverified_phone")
        val unverifiedPhone: String? = null,
    ) {
        fun toPhoneConfirmation(): AuthorizationResult.PhoneConfirmation {
            val phone = unverifiedPhone
                ?.takeIf { it.isNotBlank() }
                ?.let { PhoneNumber.create(it) }
            return AuthorizationResult.PhoneConfirmation(
                phone = phone,
                isConfirmed = isPhoneVerified ?: true,
            )
        }
    }
}
