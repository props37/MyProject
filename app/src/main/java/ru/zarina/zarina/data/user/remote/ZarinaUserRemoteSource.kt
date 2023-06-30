package ru.zarina.zarina.data.user.remote

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.user.remote.api.IZarinaUserApi
import ru.zarina.zarina.data.user.remote.api.dto.toSetCityDto
import ru.zarina.zarina.domain.City

@Factory
class ZarinaUserRemoteSource(
    private val api: IZarinaUserApi,
) : IUserRemoteSource {

    override suspend fun setCity(city: City) {
        api.setCity(city.toSetCityDto())
    }

}
