package ru.zarina.zarina.data.user.remote

import ru.zarina.zarina.data.user.remote.api.IZarinaUserApi
import ru.zarina.zarina.data.user.remote.api.dto.toSetCityDto
import ru.zarina.zarina.domain.City
import javax.inject.Inject

class ZarinaUserRemoteSource @Inject constructor(
    private val api: IZarinaUserApi,
) : IUserRemoteSource {

    override suspend fun setCity(city: City) {
        api.setCity(city.toSetCityDto())
    }

}
