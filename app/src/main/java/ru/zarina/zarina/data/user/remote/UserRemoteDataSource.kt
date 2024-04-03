package ru.zarina.zarina.data.user.remote

import ru.zarina.zarina.data.user.remote.api.UserApi
import ru.zarina.zarina.domain.common.Email
import ru.zarina.zarina.domain.common.PhoneNumber
import ru.zarina.zarina.domain.common.Token
import ru.zarina.zarina.domain.geography.City
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
}
