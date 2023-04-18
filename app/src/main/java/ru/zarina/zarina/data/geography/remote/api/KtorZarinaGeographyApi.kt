package ru.zarina.zarina.data.geography.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.zarina.zarina.data.geography.remote.api.dto.CityDto
import javax.inject.Inject

class KtorZarinaGeographyApi @Inject constructor(
    private val client: HttpClient,
) : IZarinaGeographyApi {

    override suspend fun getCity(latitude: Double, longitude: Double): CityDto {
        val response = client.get("/api/location/city") {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
        }
        return response.body()
    }

}
