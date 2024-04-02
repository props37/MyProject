package ru.zarina.zarina.data.user.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.put
import ru.zarina.zarina.data.geography.remote.api.dto.SetUserCityRequestBody
import ru.zarina.zarina.data.geography.remote.api.dto.SignUpRequestBody
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.common.Email
import ru.zarina.zarina.domain.common.PhoneNumber
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject

class UserApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
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
    ) {
        val body = SignUpRequestBody(
            firstName = firstName,
            email = email.value,
            phone = phone.value,
            password = password,
            receiveNewsByEmail = receiveNewsByEmail,
            receiveSmsNotifications = receiveSmsNotifications,
            recaptchaKey = "", // TODO: [High] Implement
        )
        httpClient.post("/api/register") {
            setJsonBody(body)
        }
    }
}
