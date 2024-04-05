package ru.livetyping.zarina.data.user.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import ru.livetyping.zarina.data.geography.remote.api.dto.SetUserCityRequestBody
import ru.livetyping.zarina.data.user.remote.api.dto.AuthorizationDto
import ru.livetyping.zarina.data.user.remote.api.dto.ConfirmSignUpRequestBody
import ru.livetyping.zarina.data.user.remote.api.dto.SignUpErrorDtoSerializer
import ru.livetyping.zarina.data.user.remote.api.dto.SignUpFieldValidationErrorDto
import ru.livetyping.zarina.data.user.remote.api.dto.SignUpMessageErrorDto
import ru.livetyping.zarina.data.user.remote.api.dto.SignUpRequestBody
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.Token
import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.user.exception.EmailAlreadyInUseException
import ru.livetyping.zarina.domain.user.exception.InvalidCaptchaException
import ru.livetyping.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject

class UserApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
    private val json: Json,
) {
    suspend fun setUserCity(city: City) {
        val body = SetUserCityRequestBody(city.kladrId.value)
        httpClient.put("/api/location/city") {
            setJsonBody(body)
        }
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
        val body = SignUpRequestBody(
            firstName = firstName,
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

    // TODO: [High] Handle exceptions
    suspend fun confirmSignUp(phone: PhoneNumber, otp: String): AuthorizationDto {
        val body = ConfirmSignUpRequestBody(phone.value, otp)
        return httpClient.post("/api/register/phone/sms/confirmation") {
            setJsonBody(body)
        }.body()
    }

    // TODO: [Low] Extract?
    private suspend inline fun <T> signUpApiExceptionConverter(block: () -> T): T {
        return try {
            block()
        } catch (e: ClientRequestException) {
            val responseText = e.response.bodyAsText()

            val element = json.parseToJsonElement(responseText)
            if (element is JsonArray) {
                val validationErrorDtos = json.decodeFromString(
                    deserializer = ListSerializer(SignUpFieldValidationErrorDto.serializer()),
                    string = responseText,
                )
                val validationExceptions = validationErrorDtos.map { it.toValidationException() }
                val validationException = ValidationException.from(validationExceptions)
                if (validationException != null) {
                    throw validationException
                } else {
                    throw e
                }
            } else {
                val errorDto =
                    json.decodeFromString(SignUpErrorDtoSerializer(), responseText)
                when (errorDto) {
                    is SignUpFieldValidationErrorDto -> throw errorDto.toValidationException()
                    is SignUpMessageErrorDto -> {
                        val message = checkNotNull(errorDto.message) { "message is null" }
                        when (message) {
                            SIGN_UP_ERROR_MESSAGE_RECAPTCHA_INVALID -> {
                                throw InvalidCaptchaException()
                            }

                            SIGN_UP_ERROR_MESSAGE_EMAIL_ALREADY_IN_USE -> {
                                throw EmailAlreadyInUseException()
                            }

                            else -> throw e
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val SIGN_UP_ERROR_MESSAGE_RECAPTCHA_INVALID = "Recaptcha not valid"
        private const val SIGN_UP_ERROR_MESSAGE_EMAIL_ALREADY_IN_USE = "Пользователь с таким Email уже зарегистрирован"
    }
}
