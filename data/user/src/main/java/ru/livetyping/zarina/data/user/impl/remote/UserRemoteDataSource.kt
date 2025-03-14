package ru.livetyping.zarina.data.user.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptcha
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.user.AuthResult
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.domain.model.user.LoyaltyProgramBonusAction
import ru.livetyping.zarina.core.domain.model.user.User
import java.time.LocalDate

internal interface UserRemoteDataSource {
    fun getUserFlow(): Flow<User>

    fun getUserCityFlow(): Flow<City>

    suspend fun setUserCity(city: City)

    fun getLoyaltyCardFlow(): Flow<LoyaltyCard>

    fun getLoyaltyProgramBonusHistoryPageFlow(page: Int): Flow<Page<List<LoyaltyProgramBonusAction>>>

    fun getLoyaltyProgramExpectedBonusesFlow(page: Int): Flow<Page<List<LoyaltyProgramBonusAction>>>

    suspend fun signIn(
        email: Email,
        password: String,
        yandexCaptchaToken: YandexCaptchaToken,
    ): AuthResult

    suspend fun signIn(phone: PhoneNumber, yandexCaptchaToken: YandexCaptchaToken)

    suspend fun confirmSignInByPhone(phone: PhoneNumber, otp: String): AuthResult

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

    suspend fun confirmSignUp(phone: PhoneNumber, otp: String): AuthResult

    suspend fun requestNewAuthOtp(phone: PhoneNumber, yandexCaptchaToken: YandexCaptchaToken)

    suspend fun updateUserInfo(
        firstName: String,
        lastName: String,
        birthDate: LocalDate,
        email: Email,
        phone: PhoneNumber,
        gender: Gender?,
        oldPassword: String?,
        newPassword: String?,
    )

    suspend fun requestPasswordReset(email: Email)

    suspend fun changePhoneNumber(phone: PhoneNumber, yandexCaptchaToken: YandexCaptchaToken)

    suspend fun confirmPhoneNumberChange(phone: PhoneNumber, otp: String)

    suspend fun requestSignInByEmailConfirmation(
        phone: PhoneNumber,
        yandexCaptchaToken: YandexCaptchaToken,
    )

    suspend fun confirmSignInByEmail(phone: PhoneNumber, otp: String): AuthResult

    suspend fun requestNewSignInByEmailConfirmationOtp(phone: PhoneNumber)

    suspend fun requestNewPhoneNumberChangeOtp(phone: PhoneNumber)

    suspend fun updateUserNotificationSettings(
        receiveSms: Boolean,
        receiveEmails: Boolean,
    )

    suspend fun signOut(): BearerTokens

    suspend fun deleteAccount()

    fun getYandexCaptcha(): YandexCaptcha
}
