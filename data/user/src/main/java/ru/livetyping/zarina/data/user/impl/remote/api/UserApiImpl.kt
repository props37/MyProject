package ru.livetyping.zarina.data.user.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
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
import ru.livetyping.zarina.data.user.impl.remote.api.dto.ConfirmSignInRequestBody
import ru.livetyping.zarina.data.user.impl.remote.api.dto.ConfirmSignUpRequestBody
import ru.livetyping.zarina.data.user.impl.remote.api.dto.GetLoyaltyCardDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.LoyaltyProgramBonusHistoryDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.NotificationSettingsDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.RequestNewAuthOtpRequestBody
import ru.livetyping.zarina.data.user.impl.remote.api.dto.RequestPasswordResetRequestBody
import ru.livetyping.zarina.data.user.impl.remote.api.dto.SetUserCityRequestBody
import ru.livetyping.zarina.data.user.impl.remote.api.dto.SignInRequestBody
import ru.livetyping.zarina.data.user.impl.remote.api.dto.SignOutDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.SignUpRequestBody
import ru.livetyping.zarina.data.user.impl.remote.api.dto.UserDto
import ru.livetyping.zarina.data.user.impl.remote.api.exception.ConfirmSignUpApiExceptionConverter
import ru.livetyping.zarina.data.user.impl.remote.api.exception.RequestPasswordResetApiExceptionConverter
import ru.livetyping.zarina.data.user.impl.remote.api.exception.SignInApiExceptionConverter
import ru.livetyping.zarina.data.user.impl.remote.api.exception.SignUpApiExceptionConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

internal class UserApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
    private val signInApiExceptionConverter: SignInApiExceptionConverter,
    private val signUpApiExceptionConverter: SignUpApiExceptionConverter,
    private val confirmSignUpApiExceptionConverter: ConfirmSignUpApiExceptionConverter,
    private val requestPasswordResetApiExceptionConverter: RequestPasswordResetApiExceptionConverter,
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

    override suspend fun getLoyaltyProgramBonusHistory(page: Int): LoyaltyProgramBonusHistoryDto {
        return httpClient.get("/api/v1/card/history") {
            parameter("page", page)
            parameter("page_size", LOYALTY_PROGRAM_BONUS_HISTORY_PAGE_SIZE)
        }.body()
    }

    override suspend fun getLoyaltyProgramExpectedBonuses(page: Int): LoyaltyProgramBonusHistoryDto {
        return httpClient.get("/api/v1/card/history/expected") {
            parameter("page", page)
            parameter("page_size", LOYALTY_PROGRAM_BONUS_HISTORY_PAGE_SIZE)
        }.body()
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

    override suspend fun confirmSignIn(phone: PhoneNumber, otp: String): AuthDto {
        val body = ConfirmSignInRequestBody(phone.value, otp)
        return confirmSignUpApiExceptionConverter {
            httpClient.post("/api/auth/phone/sms/confirmation") {
                setJsonBody(body)
            }.body()
        }
    }

    override suspend fun signUp(
        firstName: String,
        birthDate: LocalDate,
        email: Email,
        phone: PhoneNumber,
        password: String,
        receiveEmails: Boolean,
        receiveSms: Boolean,
        yandexCaptchaToken: YandexCaptchaToken,
    ) {
        val body = SignUpRequestBody(
            firstName = firstName,
            birthDate = birthDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN)),
            email = email.value,
            phone = phone.value,
            password = password,
            receiveEmails = receiveEmails,
            receiveSms = receiveSms,
            yandexCaptchaToken = yandexCaptchaToken.value,
        )
        signUpApiExceptionConverter {
            httpClient.post("/api/register") {
                setJsonBody(body)
            }
        }
    }

    override suspend fun confirmSignUp(phone: PhoneNumber, otp: String): AuthDto {
        val body = ConfirmSignUpRequestBody(phone.value, otp)
        return confirmSignUpApiExceptionConverter {
            httpClient.post("/api/register/phone/sms/confirmation") {
                setJsonBody(body)
            }.body()
        }
    }

    override suspend fun requestNewAuthOtp(
        phone: PhoneNumber,
        yandexCaptchaToken: YandexCaptchaToken,
    ) {
        val body = RequestNewAuthOtpRequestBody(phone.value, yandexCaptchaToken.value)
        httpClient.post("/api/auth/phone/sms") {
            setJsonBody(body)
        }
    }

    override suspend fun requestPasswordReset(email: Email) {
        val body = RequestPasswordResetRequestBody(email.value)
        requestPasswordResetApiExceptionConverter {
            httpClient.post("/api/auth/password") {
                setJsonBody(body)
            }
        }
    }

    override suspend fun updateUserNotificationSettings(
        receiveSms: Boolean,
        receiveEmails: Boolean
    ) {
        val body = listOf(
            NotificationSettingsDto(
                name = NotificationSettingsDto.NAME_RECEIVE_SMS,
                receiveSms,
            ),
            NotificationSettingsDto(
                name = NotificationSettingsDto.NAME_RECEIVE_EMAILS,
                receiveEmails,
            ),
        )
        httpClient.post("/api/notifications") {
            setJsonBody(body)
        }
    }

    override suspend fun signOut(): SignOutDto {
        return httpClient.delete("/api/auth").body()
    }

    override suspend fun deleteAccount() {
        httpClient.post("/api/profile/delete")
    }

    override fun getYandexCaptcha(): YandexCaptcha {
        val url = Url.create("$baseUrl/api/v1/smartCaptcha/")
        return YandexCaptcha(url)
    }

    private companion object {
        private const val DATE_PATTERN = "dd.MM.yyyy"
        private const val LOYALTY_PROGRAM_BONUS_HISTORY_PAGE_SIZE = 20
    }
}
