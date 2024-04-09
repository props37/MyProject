package ru.livetyping.zarina.data.user.remote

import ru.livetyping.zarina.data.user.remote.api.UserApi
import ru.livetyping.zarina.domain.authorization.AuthorizationResult
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.Token
import ru.livetyping.zarina.domain.geography.City
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val api: UserApi,
) {
    suspend fun setUserCity(city: City) {
        api.setUserCity(city)
    }

    suspend fun signUp(
        firstName: String,
        email: Email,
        phone: PhoneNumber,
        password: String,
        receiveNewsByEmail: Boolean,
        receiveSmsNotifications: Boolean,
        recaptchaToken: Token,
    ) {
        api.signUp(
            firstName = firstName,
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

    suspend fun requestResendSmsOtp(phone: PhoneNumber) {
        api.requestResendSmsOtp(phone)
    }
}
