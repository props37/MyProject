package ru.livetyping.zarina.data.user.impl.remote.api

import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptcha
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.network.zarina.dto.CityDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.AuthDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.GetLoyaltyCardDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.SignOutDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.UserDto
import java.time.LocalDate

internal interface UserApi {
    suspend fun getUser(): UserDto

    suspend fun getUserCity(): CityDto

    suspend fun setUserCity(city: City)

    suspend fun getLoyaltyCard(): GetLoyaltyCardDto

    suspend fun signIn(
        email: Email,
        password: String,
        yandexCaptchaToken: YandexCaptchaToken,
    ): AuthDto

    suspend fun signIn(phone: PhoneNumber, yandexCaptchaToken: YandexCaptchaToken)

    suspend fun confirmSignIn(phone: PhoneNumber, otp: String): AuthDto

    suspend fun signUp(
        firstName: String,
        birthDate: LocalDate,
        email: Email,
        phone: PhoneNumber,
        password: String,
        receiveEmails: Boolean,
        receiveSms: Boolean,
        yandexCaptchaToken: YandexCaptchaToken,
    )

    suspend fun confirmSignUp(phone: PhoneNumber, otp: String): AuthDto

    suspend fun requestNewAuthOtp(phone: PhoneNumber)

    suspend fun requestPasswordReset(email: Email)

    suspend fun updateUserNotificationSettings(
        receiveSms: Boolean,
        receiveEmails: Boolean,
    )

    suspend fun signOut(): SignOutDto

    suspend fun deleteAccount()

    fun getYandexCaptcha(): YandexCaptcha
}
