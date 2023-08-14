package ru.zarina.zarina.data.user.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.user.remote.api.dto.SetCityBody
import ru.zarina.zarina.di.Qualifiers

@Factory
class KtorZarinaUserApi(
    @Named(Qualifiers.Api.ZARINA_RESTRICTED)
    private val client: HttpClient,
) : IZarinaUserApi {

    override suspend fun setCity(body: SetCityBody) {
        client.put("/api/location/city") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }
}
