package ru.livetyping.zarina.data.user.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.user.remote.api.UserApi
import ru.livetyping.zarina.domain.authorization.AuthorizationResult
import ru.livetyping.zarina.domain.authorization.AuthorizationTokens
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.Token
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.user.LoyaltyCard
import java.time.LocalDate
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val api: UserApi,
) {
    fun getLoyaltyCardFlow(): Flow<LoyaltyCard> = flow {
        val dto = api.getLoyaltyCard()
        emit(dto.toLoyaltyCard())
    }

    suspend fun setUserCity(city: City) {
        api.setUserCity(city)
    }

    suspend fun signUp(
        firstName: String,
        birthDate: LocalDate,
        email: Email,
        phone: PhoneNumber,
        password: String,
        receiveNewsByEmail: Boolean,
        receiveSmsNotifications: Boolean,
        recaptchaToken: Token,
    ) {
        api.signUp(
            firstName = firstName,
            birthDate = birthDate,
            email = email,
            phone = phone,
            password = password,
            receiveNewsByEmail = receiveNewsByEmail,
            receiveSmsNotifications = receiveSmsNotifications,
            recaptchaToken = recaptchaToken,
        )
    }

    suspend fun confirmSignUp(phone: PhoneNumber, otp: String): AuthorizationResult {
        return api.confirmSignUp(phone, otp).toAuthorizationResult()
    }

    suspend fun signIn(email: Email, password: String, recaptchaToken: Token): AuthorizationResult {
        return api.signIn(email, password, recaptchaToken).toAuthorizationResult()
    }

    suspend fun signIn(phone: PhoneNumber, recaptchaToken: Token) {
        return api.signIn(phone, recaptchaToken)
    }

    suspend fun confirmSignInByPhone(phone: PhoneNumber, otp: String): AuthorizationResult {
        return api.confirmSignInByPhone(phone, otp).toAuthorizationResult()
    }

    suspend fun requestResendSmsOtp(phone: PhoneNumber) {
        api.requestResendSmsOtp(phone)
    }

    suspend fun requestPasswordReset(email: Email) {
        api.requestPasswordReset(email)
    }

    suspend fun signOut(): AuthorizationTokens {
        return api.signOut().toAuthorizationTokens()
    }

    suspend fun deleteAccount() {
        api.deleteAccount()
    }
}
