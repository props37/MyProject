package ru.livetyping.zarina.data.user.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptcha
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.user.AuthResult
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.data.user.impl.remote.api.UserApi
import java.time.LocalDate
import javax.inject.Inject

internal class UserRemoteDataSourceImpl @Inject constructor(
    private val api: UserApi,
) : UserRemoteDataSource {
    override fun getUserFlow(): Flow<User> = flow {
        val user = api.getUser().toUser()
        emit(user)
    }

    override fun getUserCityFlow(): Flow<City> = flow {
        val city = api.getUserCity().toCity()
        checkNotNull(city) { "city is null" }
        emit(city)
    }

    override suspend fun setUserCity(city: City) {
        api.setUserCity(city)
    }

    override fun getLoyaltyCardFlow(): Flow<LoyaltyCard> = flow {
        val dto = api.getLoyaltyCard()
        emit(dto.toLoyaltyCard())
    }

    override suspend fun signIn(
        email: Email,
        password: String,
        yandexCaptchaToken: YandexCaptchaToken,
    ): AuthResult {
        return api.signIn(email, password, yandexCaptchaToken).toAuthorizationResult()
    }

    override suspend fun signIn(phone: PhoneNumber, yandexCaptchaToken: YandexCaptchaToken) {
        api.signIn(phone, yandexCaptchaToken)
    }

    override suspend fun signUp(
        firstName: String,
        birthDate: LocalDate,
        email: Email,
        phone: PhoneNumber,
        password: String,
        receiveEmails: Boolean,
        receiveSms: Boolean,
        yandexCaptchaToken: YandexCaptchaToken
    ) {
        api.signUp(
            firstName = firstName,
            birthDate = birthDate,
            email = email,
            phone = phone,
            password = password,
            receiveEmails = receiveEmails,
            receiveSms = receiveSms,
            yandexCaptchaToken = yandexCaptchaToken,
        )
    }

    override fun getYandexCaptcha(): YandexCaptcha {
        return api.getYandexCaptcha()
    }
}
