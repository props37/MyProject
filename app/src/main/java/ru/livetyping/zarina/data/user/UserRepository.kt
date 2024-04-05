package ru.livetyping.zarina.data.user

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.user.local.UserLocalDataSource
import ru.livetyping.zarina.data.user.remote.UserRemoteDataSource
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.Gender
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.Token
import ru.livetyping.zarina.domain.geography.City
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource,
) {
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

    suspend fun confirmSignUp(phone: PhoneNumber, otp: String) {
        remoteDataSource.confirmSignUp(phone, otp)
    }

    suspend fun clear() {
        localDataSource.clear()
    }
}
