package ru.livetyping.zarina.data.user.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.util.DATE_BACKEND_PATTERN
import ru.livetyping.zarina.data.geography.remote.api.dto.SetUserCityRequestBody
import ru.livetyping.zarina.data.user.remote.api.dto.AuthorizationDto
import ru.livetyping.zarina.data.user.remote.api.dto.ConfirmSignInByPhoneRequestBody
import ru.livetyping.zarina.data.user.remote.api.dto.ConfirmSignUpRequestBody
import ru.livetyping.zarina.data.user.remote.api.dto.GetLoyaltyCardDto
import ru.livetyping.zarina.data.user.remote.api.dto.LoyaltyProgramBonusHistoryDto
import ru.livetyping.zarina.data.user.remote.api.dto.RequestPasswordResetRequestBody
import ru.livetyping.zarina.data.user.remote.api.dto.RequestResendSmsOtpRequestBody
import ru.livetyping.zarina.data.user.remote.api.dto.SignInRequestBody
import ru.livetyping.zarina.data.user.remote.api.dto.SignOutDto
import ru.livetyping.zarina.data.user.remote.api.dto.SignUpRequestBody
import ru.livetyping.zarina.data.user.remote.api.dto.UserDto
import ru.livetyping.zarina.data.user.remote.api.exception.ConfirmSignUpApiExceptionConverter
import ru.livetyping.zarina.data.user.remote.api.exception.RequestPasswordResetApiExceptionConverter
import ru.livetyping.zarina.data.user.remote.api.exception.SignInApiExceptionConverter
import ru.livetyping.zarina.data.user.remote.api.exception.SignUpApiExceptionConverter
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.Token
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.util.library.ktor.setJsonBody
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UserApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
    private val signUpApiExceptionConverter: SignUpApiExceptionConverter,
    private val confirmSignUpApiExceptionConverter: ConfirmSignUpApiExceptionConverter,
    private val signInApiExceptionConverter: SignInApiExceptionConverter,
    private val requestPasswordResetApiExceptionConverter: RequestPasswordResetApiExceptionConverter,
) {
    suspend fun getUser(): UserDto {
        return httpClient.get("/api/v1/profile").body()
    }

    suspend fun getLoyaltyCard(): GetLoyaltyCardDto {
        return httpClient.get("/api/card").body()
    }

    suspend fun getLoyaltyCardBonusHistory(page: Int): LoyaltyProgramBonusHistoryDto {
        return httpClient.get("/api/v1/card/history") {
            parameter("page", page)
            parameter("page_size", LOYALTY_PROGRAM_BONUS_HISTORY_PAGE_SIZE)
        }.body()
    }

    suspend fun getLoyaltyCardExpectedBonuses(page: Int): LoyaltyProgramBonusHistoryDto {
        return httpClient.get("/api/v1/card/history/expected") {
            parameter("page", page)
            parameter("page_size", LOYALTY_PROGRAM_BONUS_HISTORY_PAGE_SIZE)
        }.body()
    }

    suspend fun setUserCity(city: City) {
        val body = SetUserCityRequestBody(city.kladrId.value)
        httpClient.put("/api/location/city") {
            setJsonBody(body)
        }
    }

    suspend fun signUp(
        firstName: String,
        birthDate: LocalDate,
        email: Email,
        phone: PhoneNumber,
        password: String,
        receiveNewsByEmail: Boolean,
        receiveSmsNotifications: Boolean,
        recaptchaToken: Token,
    ) {
        val body = SignUpRequestBody(
            firstName = firstName,
            birthDate = birthDate.format(DateTimeFormatter.ofPattern(DATE_BACKEND_PATTERN)),
            email = email.value,
            phone = phone.value,
            password = password,
            receiveNewsByEmail = receiveNewsByEmail,
            receiveSmsNotifications = receiveSmsNotifications,
            recaptchaToken = recaptchaToken.value,
        )
        signUpApiExceptionConverter {
            httpClient.post("/api/register") {
                setJsonBody(body)
            }
        }
    }

    suspend fun confirmSignUp(phone: PhoneNumber, otp: String): AuthorizationDto {
        val body = ConfirmSignUpRequestBody(phone.value, otp)
        return confirmSignUpApiExceptionConverter {
            httpClient.post("/api/register/phone/sms/confirmation") {
                setJsonBody(body)
            }.body()
        }
    }

    suspend fun signIn(email: Email, password: String, recaptchaToken: Token): AuthorizationDto {
        val body = SignInRequestBody.Email(email.value, password, recaptchaToken.value)
        return signInApiExceptionConverter {
            httpClient.post("/api/auth/email") {
                setJsonBody(body)
            }.body()
        }
    }

    suspend fun signIn(phone: PhoneNumber, recaptchaToken: Token) {
        val body = SignInRequestBody.Phone(phone.value, recaptchaToken.value)
        signInApiExceptionConverter {
            httpClient.post("/api/auth/phone") {
                setJsonBody(body)
            }
        }
    }

    suspend fun confirmSignInByPhone(phone: PhoneNumber, otp: String): AuthorizationDto {
        val body = ConfirmSignInByPhoneRequestBody(phone.value, otp)
        return confirmSignUpApiExceptionConverter {
            httpClient.post("/api/auth/phone/sms/confirmation") {
                setJsonBody(body)
            }.body()
        }
    }

    suspend fun requestResendSmsOtp(phone: PhoneNumber) {
        val body = RequestResendSmsOtpRequestBody(phone.value)
        httpClient.post("/api/auth/phone/sms") {
            setJsonBody(body)
        }
    }

    suspend fun requestPasswordReset(email: Email) {
        val body = RequestPasswordResetRequestBody(email.value)
        requestPasswordResetApiExceptionConverter {
            httpClient.post("/api/auth/password") {
                setJsonBody(body)
            }
        }
    }

    suspend fun signOut(): SignOutDto {
        return httpClient.delete("/api/auth").body()
    }

    suspend fun deleteAccount() {
        httpClient.post("/api/profile/delete")
    }

    companion object {
        private const val LOYALTY_PROGRAM_BONUS_HISTORY_PAGE_SIZE = 20
    }
}
