package ru.livetyping.zarina.data.user.impl.remote.api

import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.data.user.impl.remote.api.dto.UserDto

internal interface UserApi {
    suspend fun getUser(): UserDto

    suspend fun setUserCity(city: City)
}
