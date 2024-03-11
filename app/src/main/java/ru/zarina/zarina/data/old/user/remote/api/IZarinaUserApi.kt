package ru.zarina.zarina.data.old.user.remote.api

import ru.zarina.zarina.data.old.user.remote.api.dto.SetCityBody

interface IZarinaUserApi {

    suspend fun setCity(body: SetCityBody)

}
