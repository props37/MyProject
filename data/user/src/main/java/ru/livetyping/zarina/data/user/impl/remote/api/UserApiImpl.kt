package ru.livetyping.zarina.data.user.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import ru.livetyping.zarina.core.buildutil.ZarinaBaseUrl
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptcha
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.core.network.util.setJsonBody
import ru.livetyping.zarina.core.network.zarina.dto.CityDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.AuthDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.GetLoyaltyCardDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.SetUserCityRequestBody
import ru.livetyping.zarina.data.user.impl.remote.api.dto.SignInRequestBody
import ru.livetyping.zarina.data.user.impl.remote.api.dto.UserDto
import ru.livetyping.zarina.data.user.impl.remote.api.exception.SignInApiExceptionConverter
import javax.inject.Inject

internal class UserApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
    private val signInApiExceptionConverter: SignInApiExceptionConverter,
    @ZarinaBaseUrl
    private val baseUrl: String,
) : UserApi {
    override suspend fun getUser(): UserDto {
        return httpClient.get("/api/v1/profile").body()
    }

    override suspend fun getUserCity(): CityDto {
        return httpClient.post("api/v1/location/city").body()
    }

    override suspend fun setUserCity(city: City) {
        val body = SetUserCityRequestBody(city.id.value)
        httpClient.put("/api/location/city") {
            setJsonBody(body)
        }
    }

    override suspend fun getLoyaltyCard(): GetLoyaltyCardDto {
        return httpClient.get("/api/card").body()
    }

    override suspend fun signIn(
        email: Email,
        password: String,
        yandexCaptchaToken: YandexCaptchaToken,
    ): AuthDto {
        val body = SignInRequestBody.Email(
            email = email.value,
            password = password,
            yandexCaptchaToken = yandexCaptchaToken.value,
        )
        return signInApiExceptionConverter {
            httpClient.post("/api/auth/email") {
                setJsonBody(body)
            }.body()
        }
    }

    override suspend fun signIn(phone: PhoneNumber, yandexCaptchaToken: YandexCaptchaToken) {
        val body = SignInRequestBody.Phone(
            phone = phone.value,
            yandexCaptchaToken = yandexCaptchaToken.value,
        )
        signInApiExceptionConverter {
            httpClient.post("/api/auth/phone") {
                setJsonBody(body)
            }
        }
    }

    override fun getYandexCaptcha(): YandexCaptcha {
        val url = Url.create("$baseUrl/api/v1/smartCaptcha/")
        return YandexCaptcha(url)
    }
}
