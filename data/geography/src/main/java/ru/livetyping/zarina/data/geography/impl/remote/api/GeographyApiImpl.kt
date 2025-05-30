package ru.livetyping.zarina.data.geography.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.geo.FiasId
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.core.network.zarina.dto.CityDto
import ru.livetyping.zarina.data.geography.impl.remote.api.dto.BuildingDto
import ru.livetyping.zarina.data.geography.impl.remote.api.dto.StreetDto
import ru.livetyping.zarina.data.geography.impl.remote.api.exception.AddressApiExceptionConverter
import javax.inject.Inject

internal class GeographyApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
    private val addressApiExceptionConverter: AddressApiExceptionConverter,
) : GeographyApi {
    override suspend fun getCityByLocation(location: Location): CityDto {
        return httpClient.post("/api/v1/location/city") {
            parameter("latitude", location.latitude)
            parameter("longitude", location.longitude)
        }.body()
    }

    override suspend fun getCities(nameQuery: String?): List<CityDto> {
        return httpClient.get("/api/location/city/list") {
            parameter("name", nameQuery)
        }.body()
    }

    override suspend fun getCityStreets(cityFiasId: FiasId, nameQuery: String): List<StreetDto> {
        return addressApiExceptionConverter {
            httpClient.get("/api/adresses/suggest/street") {
                parameter("city_id", cityFiasId.value)
                parameter("name", nameQuery)
            }.body()
        }
    }

    override suspend fun getStreetBuildings(
        streetFiasId: FiasId,
        nameQuery: String,
    ): List<BuildingDto> {
        return addressApiExceptionConverter {
            httpClient.get("/api/adresses/suggest/buildings") {
                parameter("city_id", streetFiasId.value)
                parameter("name", nameQuery)
            }.body()
        }
    }
}
