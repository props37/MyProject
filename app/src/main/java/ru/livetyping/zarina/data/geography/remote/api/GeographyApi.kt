package ru.livetyping.zarina.data.geography.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import ru.livetyping.zarina.data.geography.remote.api.dto.BuildingDto
import ru.livetyping.zarina.data.geography.remote.api.dto.CityDto
import ru.livetyping.zarina.data.geography.remote.api.dto.StreetDto
import ru.livetyping.zarina.data.geography.remote.api.exception.GetAddressApiExceptionConverter
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.location.Location
import javax.inject.Inject

class GeographyApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
    private val getAddressApiExceptionConverter: GetAddressApiExceptionConverter,
) {
    suspend fun getCity(location: Location): CityDto {
        return httpClient.post("/api/v1/location/city") {
            parameter("latitude", location.latitude)
            parameter("longitude", location.longitude)
        }.body()
    }

    suspend fun getCities(nameQuery: String?): List<CityDto> {
        return httpClient.get("/api/location/city/list") {
            parameter("name", nameQuery)
        }.body()
    }

    suspend fun getCityStreets(cityKladrId: KladrId, nameQuery: String): List<StreetDto> {
        return getAddressApiExceptionConverter {
            httpClient.get("/api/adresses/suggest/street") {
                parameter("city_id", cityKladrId.value)
                parameter("name", nameQuery)
            }.body()
        }
    }

    suspend fun getStreetBuildings(streetKladrId: KladrId, nameQuery: String): List<BuildingDto> {
        return getAddressApiExceptionConverter {
            httpClient.get("/api/adresses/suggest/buildings") {
                parameter("city_id", streetKladrId.value)
                parameter("name", nameQuery)
            }.body()
        }
    }
}
