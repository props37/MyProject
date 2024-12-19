package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptcha
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.user.AuthResult
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.domain.model.user.User
import java.time.LocalDate

public interface UserRepository {
    public fun getUserFlow(cachePolicy: CachePolicy): Flow<User?>

    public suspend fun setUser(user: User)

    public fun getUserCityFlow(cachePolicy: CachePolicy): Flow<City?>

    public suspend fun setUserCity(city: City)

    public suspend fun setLocalUserCity(city: City)

    public fun getLoyaltyCardFlow(cachePolicy: CachePolicy): Flow<LoyaltyCard?>

    public suspend fun signIn(
        email: Email,
        password: String,
        yandexCaptchaToken: YandexCaptchaToken,
    ): AuthResult

    public suspend fun signIn(phone: PhoneNumber, yandexCaptchaToken: YandexCaptchaToken)

    public suspend fun confirmSignIn(phone: PhoneNumber, otp: String): AuthResult

    public suspend fun signUp(
        firstName: String,
        birthDate: LocalDate,
        email: Email,
        phone: PhoneNumber,
        password: String,
        receiveEmails: Boolean,
        receiveSms: Boolean,
        yandexCaptchaToken: YandexCaptchaToken,
    )

    public suspend fun confirmSignUp(phone: PhoneNumber, otp: String): AuthResult

    public suspend fun requestNewAuthOtp(phone: PhoneNumber, yandexCaptchaToken: YandexCaptchaToken)

    public suspend fun requestPasswordReset(email: Email)

    public suspend fun updateUserNotificationSettings(
        receiveSms: Boolean,
        receiveEmails: Boolean,
    )

    public suspend fun signOut(): BearerTokens

    public suspend fun deleteAccount()

    public fun getYandexCaptcha(): YandexCaptcha

    public suspend fun clear()
}
