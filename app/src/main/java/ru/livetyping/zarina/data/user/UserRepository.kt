package ru.livetyping.zarina.data.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.data.user.local.UserLocalDataSource
import ru.livetyping.zarina.data.user.remote.UserRemoteDataSource
import ru.livetyping.zarina.domain.authorization.AuthorizationResult
import ru.livetyping.zarina.domain.authorization.AuthorizationTokens
import ru.livetyping.zarina.domain.captcha.YandexCaptchaToken
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.Gender
import ru.livetyping.zarina.domain.common.Page
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.user.LoyaltyCard
import ru.livetyping.zarina.domain.user.LoyaltyProgramBonusAction
import ru.livetyping.zarina.domain.user.User
import java.time.LocalDate
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource,
) {
    fun getUserFlow(): Flow<User?> {
        return localDataSource.getUserFlow()
    }

    fun getUpdatedUserFlow(): Flow<User> {
        return remoteDataSource.getUserFlow()
            .onEach(::setUser)
    }

    suspend fun setUser(user: User) {
        localDataSource.setUser(user)
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
        remoteDataSource.updateUserInfo(
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

        // TODO: [Backend] Refactor when backend starts to return a user as a response
        val user = remoteDataSource.getUserFlow().firstOrNull()
        if (user != null) setUser(user)
    }

    suspend fun changePhoneNumber(phone: PhoneNumber, yandexCaptchaToken: YandexCaptchaToken) {
        remoteDataSource.changePhoneNumber(phone, yandexCaptchaToken)
    }

    suspend fun confirmPhoneNumberChange(phone: PhoneNumber, code: String) {
        remoteDataSource.confirmPhoneNumberChange(phone, code)
    }

    suspend fun requestResendPhoneNumberChangeSmsOtp(phone: PhoneNumber) {
        remoteDataSource.requestResendPhoneNumberChangeSmsOtp(phone)
    }

    suspend fun updateUserNotificationSettings(
        receiveSms: Boolean,
        receiveEmails: Boolean,
    ) {
        remoteDataSource.updateUserNotificationSettings(receiveSms, receiveEmails)
    }

    suspend fun fetchUserCity() {
        val city = remoteDataSource.getUserCityFlow().firstOrNull()
        checkNotNull(city) { "Failed to fetch user city" }
        localDataSource.setUserCity(city)
    }

    fun getUserCityFlow(): Flow<City?> {
        return localDataSource.getUserCityFlow()
    }

    suspend fun setUserCity(city: City) {
        remoteDataSource.setUserCity(city)
        localDataSource.setUserCity(city)
    }

    suspend fun setLocalUserCity(city: City) {
        localDataSource.setUserCity(city)
    }

    fun getUserContentGenderFlow(): Flow<Gender?> {
        return localDataSource.getUserContentGenderFlow()
    }

    suspend fun setUserContentGender(gender: Gender) {
        localDataSource.setUserContentGender(gender)
    }

    fun getLoyaltyCardFlow(): Flow<LoyaltyCard?> {
        return localDataSource.getLoyaltyCardFlow()
    }

    fun getLoyaltyCardBonusHistoryPageFlow(page: Int): Flow<Page<List<LoyaltyProgramBonusAction>>> {
        return remoteDataSource.getLoyaltyCardBonusHistoryPageFlow(page)
    }

    fun getLoyaltyCardExpectedBonusesPageFlow(page: Int): Flow<Page<List<LoyaltyProgramBonusAction>>> {
        return remoteDataSource.getLoyaltyCardExpectedBonusesFlow(page)
    }

    suspend fun fetchLoyaltyCard() {
        val card = remoteDataSource.getLoyaltyCardFlow().firstOrNull()
        checkNotNull(card) { "Failed to fetch loyalty card" }
        localDataSource.setLoyaltyCard(card)
    }

    suspend fun signUp(
        firstName: String,
        birthDate: LocalDate,
        email: Email,
        phone: PhoneNumber,
        password: String,
        receiveEmails: Boolean,
        receiveSms: Boolean,
        yandexCaptchaToken: YandexCaptchaToken,
    ) {
        remoteDataSource.signUp(
            firstName = firstName,
            birthDate = birthDate,
            email = email,
            phone = phone,
            password = password,
            receiveNews = receiveEmails,
            receiveSms = receiveSms,
            yandexCaptchaToken = yandexCaptchaToken,
        )
    }

    suspend fun confirmSignUp(phone: PhoneNumber, otp: String): AuthorizationResult {
        return remoteDataSource.confirmSignUp(phone, otp)
    }

    suspend fun signIn(
        email: Email,
        password: String,
        yandexCaptchaToken: YandexCaptchaToken,
    ): AuthorizationResult {
        return remoteDataSource.signIn(email, password, yandexCaptchaToken)
    }

    suspend fun signIn(phone: PhoneNumber, yandexCaptchaToken: YandexCaptchaToken) {
        remoteDataSource.signIn(phone, yandexCaptchaToken)
    }

    suspend fun confirmSignInByPhone(phone: PhoneNumber, otp: String): AuthorizationResult {
        return remoteDataSource.confirmSignInByPhone(phone, otp)
    }

    suspend fun requestResendAuthorizationSmsOtp(
        phone: PhoneNumber,
        yandexCaptchaToken: YandexCaptchaToken,
    ) {
        remoteDataSource.requestResendAuthorizationSmsOtp(phone, yandexCaptchaToken)
    }

    suspend fun requestPasswordReset(email: Email) {
        remoteDataSource.requestPasswordReset(email)
    }

    suspend fun signOut(): AuthorizationTokens {
        return remoteDataSource.signOut()
    }

    suspend fun deleteAccount() {
        remoteDataSource.deleteAccount()
    }

    suspend fun clear() {
        localDataSource.clear()
    }
}
