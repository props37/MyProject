package ru.livetyping.zarina.data.user.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.user.AuthResult
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.core.network.zarina.dto.BearerTokensDto

@Serializable
internal data class AuthDto(
    @SerialName("user")
    val user: UserDto? = null,

    @SerialName("jwt")
    val jwt: BearerTokensDto? = null,

    @SerialName("phone_verification")
    val phoneVerification: PhoneConfirmationDto? = null,
) {
    fun toAuthResult(): AuthResult {
        checkPropertyNotNull(jwt) { ::jwt }
        checkPropertyNotNull(user) { ::user }
        return AuthResult(
            tokens = jwt.toBearerTokens(),
            user = user.toUser(),
            phoneConfirmation = phoneVerification?.toPhoneConfirmation(),
        )
    }

    @Serializable
    data class PhoneConfirmationDto(
        @SerialName("phone_is_verified")
        val phoneIsVerified: Boolean? = null,

        @SerialName("unverified_phone")
        val unverifiedPhone: String? = null,
    ) {
        fun toPhoneConfirmation(): AuthResult.PhoneConfirmation {
            val phone = unverifiedPhone
                ?.takeIf { it.isNotBlank() }
                ?.let { PhoneNumber.create(it) }
            return AuthResult.PhoneConfirmation(
                phone = phone,
                isConfirmed = phoneIsVerified ?: true,
            )
        }
    }
}
