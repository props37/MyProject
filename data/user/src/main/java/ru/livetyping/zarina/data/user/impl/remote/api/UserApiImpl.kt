package ru.livetyping.zarina.data.user.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.request.put
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.core.network.util.setJsonBody
import ru.livetyping.zarina.data.user.impl.remote.api.dto.SetUserCityRequestBody
import javax.inject.Inject

internal class UserApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) : UserApi {
    override suspend fun setUserCity(city: City) {
        val body = SetUserCityRequestBody(city.id.value)
        httpClient.put("/api/location/city") {
            setJsonBody(body)
        }
    }
}
