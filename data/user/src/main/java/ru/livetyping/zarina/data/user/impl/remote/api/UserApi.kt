package ru.livetyping.zarina.data.user.impl.remote.api

import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptcha
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.network.zarina.dto.CityDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.AuthDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.GetLoyaltyCardDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.LoyaltyProgramBonusHistoryDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.SignOutDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.UserDto
import java.time.LocalDate

internal interface UserApi {
    suspend fun getUser(): UserDto

    suspend fun getUserCity(): CityDto

    suspend fun setUserCity(city: City)

    suspend fun getLoyaltyCard(): GetLoyaltyCardDto

    suspend fun getLoyaltyProgramBonusHistory(page: Int): LoyaltyProgramBonusHistoryDto

    suspend fun getLoyaltyProgramExpectedBonuses(page: Int): LoyaltyProgramBonusHistoryDto

    suspend fun signIn(
        email: Email,
        password: String,
        yandexCaptchaToken: YandexCaptchaToken,
    ): AuthDto

    suspend fun signIn(phone: PhoneNumber, yandexCaptchaToken: YandexCaptchaToken)

    suspend fun requestSignInByEmailConfirmation(
        phone: PhoneNumber,
        yandexCaptchaToken: YandexCaptchaToken,
    )

    suspend fun confirmSignInByEmail(phone: PhoneNumber, otp: String): AuthDto

    suspend fun requestNewSignInByEmailConfirmationOtp(phone: PhoneNumber)

    suspend fun confirmSignInByPhone(phone: PhoneNumber, otp: String): AuthDto

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

    suspend fun requestNewPhoneNumberChangeOtp(phone: PhoneNumber)

    suspend fun updateUserNotificationSettings(
        receiveSms: Boolean,
        receiveEmails: Boolean,
    )

    suspend fun signOut(): SignOutDto

    suspend fun deleteAccount()

    fun getYandexCaptcha(): YandexCaptcha
}
