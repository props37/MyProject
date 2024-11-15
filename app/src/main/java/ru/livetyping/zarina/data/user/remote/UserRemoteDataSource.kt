package ru.livetyping.zarina.data.user.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.user.remote.api.UserApi
import ru.livetyping.zarina.domain.authorization.AuthorizationResult
import ru.livetyping.zarina.domain.authorization.AuthorizationTokens
import ru.livetyping.zarina.domain.captcha.YandexCaptchaToken
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.Gender
import ru.livetyping.zarina.domain.common.Page
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.Token
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.user.LoyaltyCard
import ru.livetyping.zarina.domain.user.LoyaltyProgramBonusAction
import ru.livetyping.zarina.domain.user.User
import java.time.LocalDate
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val api: UserApi,
) {
    fun getUserFlow(): Flow<User> = flow {
        val user = api.getUser().toUser()
        emit(user)
    }

    suspend fun updateUserInfo(
        firstName: String,
        middleName: String?,
        lastName: String,
        birthDate: LocalDate,
        email: Email,
        phone: PhoneNumber,
        gender: Gender,
        oldPassword: String?,
        newPassword: String?,
    ) {
        api.updateUserInfo(
            firstName = firstName,
            middleName = middleName,
            lastName = lastName,
            birthDate = birthDate,
            email = email,
            phone = phone,
            gender = gender,
            oldPassword = oldPassword,
            newPassword = newPassword,
        )
    }

    suspend fun changePhoneNumber(phone: PhoneNumber, yandexCaptchaToken: YandexCaptchaToken) {
        api.changePhoneNumber(phone, yandexCaptchaToken)
    }

    suspend fun confirmPhoneNumberChange(phone: PhoneNumber, code: String) {
        api.confirmPhoneNumberChange(phone, code)
    }

    suspend fun requestResendPhoneNumberChangeSmsOtp(phone: PhoneNumber) {
        api.requestResendPhoneNumberChangeSmsOtp(phone)
    }

    suspend fun updateUserNotificationSettings(
        receiveSms: Boolean,
        receiveEmails: Boolean,
    ) {
        api.updateUserNotificationSettings(receiveSms, receiveEmails)
    }

    fun getUserCityFlow(): Flow<City> = flow {
        val city = api.getUserCity().toCity()
        checkNotNull(city) { "city is null" }
        emit(city)
    }

    suspend fun setUserCity(city: City) {
        api.setUserCity(city)
    }

    fun getLoyaltyCardFlow(): Flow<LoyaltyCard> = flow {
        val dto = api.getLoyaltyCard()
        emit(dto.toLoyaltyCard())
    }

    fun getLoyaltyCardBonusHistoryPageFlow(page: Int): Flow<Page<List<LoyaltyProgramBonusAction>>> =
        flow {
            val dto = api.getLoyaltyCardBonusHistory(page)
            val bonusHistoryPage = dto.toLoyaltyProgramBonusActionPage()
            emit(bonusHistoryPage)
        }

    fun getLoyaltyCardExpectedBonusesFlow(page: Int): Flow<Page<List<LoyaltyProgramBonusAction>>> =
        flow {
            val dto = api.getLoyaltyCardExpectedBonuses(page)
            val expectedBonusesPage = dto.toLoyaltyProgramBonusActionPage()
            emit(expectedBonusesPage)
        }

    suspend fun signUp(
        firstName: String,
        birthDate: LocalDate,
        email: Email,
        phone: PhoneNumber,
        password: String,
        receiveNews: Boolean,
        receiveSms: Boolean,
        yandexCaptchaToken: YandexCaptchaToken,
    ) {
        api.signUp(
            firstName = firstName,
            birthDate = birthDate,
            email = email,
            phone = phone,
            password = password,
            receiveEmails = receiveNews,
            receiveSms = receiveSms,
            yandexCaptchaToken = yandexCaptchaToken,
        )
    }

    suspend fun confirmSignUp(phone: PhoneNumber, otp: String): AuthorizationResult {
        return api.confirmSignUp(phone, otp).toAuthorizationResult()
    }

    suspend fun signIn(
        email: Email,
        password: String,
        yandexCaptchaToken: YandexCaptchaToken,
    ): AuthorizationResult {
        return api.signIn(email, password, yandexCaptchaToken).toAuthorizationResult()
    }

    suspend fun signIn(phone: PhoneNumber, yandexCaptchaToken: YandexCaptchaToken) {
        return api.signIn(phone, yandexCaptchaToken)
    }

    suspend fun confirmSignInByPhone(phone: PhoneNumber, otp: String): AuthorizationResult {
        return api.confirmSignInByPhone(phone, otp).toAuthorizationResult()
    }

    suspend fun requestResendAuthorizationSmsOtp(phone: PhoneNumber) {
        api.requestResendAuthorizationSmsOtp(phone)
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
