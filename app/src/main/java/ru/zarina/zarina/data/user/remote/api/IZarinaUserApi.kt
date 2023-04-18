package ru.zarina.zarina.data.user.remote.api

import ru.zarina.zarina.data.user.remote.api.dto.SetCityBody

interface IZarinaUserApi {

    suspend fun setCity(body: SetCityBody)

}
