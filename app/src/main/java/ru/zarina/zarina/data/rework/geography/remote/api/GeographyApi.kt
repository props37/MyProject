package ru.zarina.zarina.data.rework.geography.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.zarina.zarina.data.rework.geography.remote.api.dto.CityDto
import ru.zarina.zarina.di.reworked.Qualifiers
import ru.zarina.zarina.domain.rework.location.Location
import javax.inject.Inject

class GeographyApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getCity(location: Location): CityDto {
        return httpClient.get("/api/location/city") {
            parameter("latitude", location.latitude)
            parameter("longitude", location.longitude)
        }.body()
    }
}
