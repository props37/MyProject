package ru.livetyping.zarina.data.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.data.user.local.UserLocalDataSource
import ru.livetyping.zarina.data.user.remote.UserRemoteDataSource
import ru.livetyping.zarina.domain.authorization.AuthorizationResult
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.Gender
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.Token
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.user.LoyaltyCard
import ru.livetyping.zarina.domain.user.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource,
) {
    fun getUserFlow(): Flow<User?> {
        return localDataSource.getUserFlow()
    }

    suspend fun setUser(user: User) {
        localDataSource.setUser(user)
    }

    fun getLoyaltyCardFlow(): Flow<LoyaltyCard?> {
        return localDataSource.getLoyaltyCardFlow()
    }

    suspend fun fetchLoyaltyCard() {
        val card = remoteDataSource.getLoyaltyCardFlow().firstOrNull()
        checkNotNull(card) { "Failed to fetch loyalty card" }
        localDataSource.setLoyaltyCard(card)
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

    suspend fun signUp(
        firstName: String,
        email: Email,
        phone: PhoneNumber,
        password: String,
        receiveNewsByEmail: Boolean,
        receiveSmsNotifications: Boolean,
        recaptchaToken: Token,
    ) {
        remoteDataSource.signUp(
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
        return remoteDataSource.confirmSignUp(phone, otp)
    }

    suspend fun signIn(email: Email, password: String, recaptchaToken: Token): AuthorizationResult {
        return remoteDataSource.signIn(email, password, recaptchaToken)
    }

    suspend fun signIn(phone: PhoneNumber, recaptchaToken: Token) {
        remoteDataSource.signIn(phone, recaptchaToken)
    }

    suspend fun confirmSignInByPhone(phone: PhoneNumber, otp: String): AuthorizationResult {
        return remoteDataSource.confirmSignInByPhone(phone, otp)
    }

    suspend fun requestResendSmsOtp(phone: PhoneNumber) {
        remoteDataSource.requestResendSmsOtp(phone)
    }

    suspend fun requestPasswordReset(email: Email) {
        remoteDataSource.requestPasswordReset(email)
    }

    suspend fun signOut() {
        remoteDataSource.signOut()
    }

    suspend fun deleteAccount() {
        remoteDataSource.deleteAccount()
    }

    suspend fun clear() {
        localDataSource.clear()
    }
}
