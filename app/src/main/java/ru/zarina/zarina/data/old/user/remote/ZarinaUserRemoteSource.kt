package ru.zarina.zarina.data.old.user.remote

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.old.user.remote.api.IZarinaUserApi
import ru.zarina.zarina.data.old.user.remote.api.dto.toSetCityDto
import ru.zarina.zarina.domain.old.City

@Factory
class ZarinaUserRemoteSource(
    private val api: IZarinaUserApi,
) : IUserRemoteSource {

    override suspend fun setCity(city: City) {
        api.setCity(city.toSetCityDto())
    }

}
