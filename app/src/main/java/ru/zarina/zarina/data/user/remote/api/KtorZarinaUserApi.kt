package ru.zarina.zarina.data.user.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import ru.zarina.zarina.data.user.remote.api.dto.SetCityBody
import ru.zarina.zarina.di.Authorization
import javax.inject.Inject

class KtorZarinaUserApi @Inject constructor(
    @Authorization(Authorization.Type.TOKEN)
    private val client: HttpClient,
) : IZarinaUserApi {

    override suspend fun setCity(body: SetCityBody) {
        client.put("/api/location/city") {
            setBody(body)
        }
    }
}
