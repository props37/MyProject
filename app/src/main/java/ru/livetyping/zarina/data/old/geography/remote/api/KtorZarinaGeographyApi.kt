package ru.livetyping.zarina.data.old.geography.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.data.old.geography.remote.api.dto.CityDto
import ru.livetyping.zarina.di.old.Qualifiers

@Factory
class KtorZarinaGeographyApi(
    @Named(Qualifiers.Api.ZARINA_RESTRICTED)
    private val client: HttpClient,
) : IZarinaGeographyApi {

    override suspend fun getCity(latitude: Double, longitude: Double): CityDto {
        val response = client.get("/api/location/city") {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
        }
        return response.body()
    }

    override suspend fun getCities(name: String?): List<CityDto> {
        val response = client.get("/api/location/city/list") {
            name?.let { parameter("name", name) }
        }
        return response.body()
    }
}
