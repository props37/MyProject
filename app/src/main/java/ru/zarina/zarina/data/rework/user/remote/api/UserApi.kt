package ru.zarina.zarina.data.rework.user.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.request.put
import ru.zarina.zarina.data.rework.geography.remote.api.dto.UpdateUserCityRequestBody
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject

class UserApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun updateUserCity(city: City) {
        val body = UpdateUserCityRequestBody(city.kladrId.value)
        httpClient.put("/api/location/city") {
            setJsonBody(body)
        }
    }
}
